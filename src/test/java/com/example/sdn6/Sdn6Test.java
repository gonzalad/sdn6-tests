package com.example.sdn6;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import static org.assertj.core.api.Assertions.assertThat;
import com.example.sdn6.entity.NoeudMaquetteAPourEnfantRelationEntity;
import com.example.sdn6.entity.NoeudMaquetteEntity;
import com.example.sdn6.projection.Enfant;
import com.example.sdn6.projection.NoeudProjection;
import com.example.sdn6.repository.NoeudMaquetteRepository;
import com.example.sdn6.spa.NoeudMaquetteServicePortAdapter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class Sdn6Test {

    @Autowired
    private NoeudMaquetteRepository repository;

    @Autowired
    private NoeudMaquetteServicePortAdapter spa;

    @BeforeEach
    void setUp() {
        repository.deleteAll();
    }

    @Test
    void testProjection() {

        // 1. Création graphe initial
        NoeudMaquetteEntity f1 = newNoeudMaquetteEntity("F1");
        NoeudMaquetteEntity of2 = newNoeudMaquetteEntity("OF2");
        NoeudMaquetteEntity of1 = newNoeudMaquetteEntity("OF1");
        of1.setLibelleLong("libelleLong");
        NoeudMaquetteEntity of11 = newNoeudMaquetteEntity("OF11");
        of1.addEnfant(of11, true);
        f1.addEnfant(of1, true);
        f1.addEnfant(of2, true);
        List<NoeudMaquetteEntity> entities = List.of(
            of2,
            of1,
            of11,
            f1
        );
        repository.saveAll(entities);

        // 2. Récupération d'un noeud et persistance du noeud
        Optional<NoeudProjection> projectionAModifier = repository.findProjectionByIdDefinition(of1.getIdDefinition());
        assertThat(projectionAModifier).isNotEmpty();
        assertThat(projectionAModifier.get().getCode()).isEqualTo("OF1");
        assertThat(projectionAModifier.get().getEnfants()).isOfAnyClassIn(Enfant.class);
        projectionAModifier.get().setLibelleCourt("modifie");
        repository.save(projectionAModifier.get());

        // 3. Vérification des relations de la formation
        Optional<NoeudMaquetteEntity> f1Lue = spa.lireNoeudAvecDescendance(f1.getIdDefinition());
        assertThat(f1Lue).isPresent();
        assertThat(f1Lue.get().getEnfants())
            .hasSize(2)
            .extracting(NoeudMaquetteAPourEnfantRelationEntity::getEnfant)
            .extracting(NoeudMaquetteEntity::getCode)
            .containsExactlyInAnyOrder(of1.getCode(), of2.getCode());
        assertThat(f1Lue.get().getEnfants())
            .extracting(NoeudMaquetteAPourEnfantRelationEntity::getEnfant)
            .filteredOn(om -> om.getCode().equals(projectionAModifier.get().getCode()))
            .element(0)
            .satisfies(om -> {
                // on vérifie que l'attribut a bien été modifié
                assertThat(om.getLibelleCourt()).isEqualTo("modifie");
                // on vérifie que les attributs n'existant pas dans NoeudProjection sont bien conservés
                assertThat(om.getLibelleLong()).isEqualTo("libelleLong");
                // la relation (of1)-[:HAS_ENFANT]->(of11) a été conservée, youhou !!!
                assertThat(om.getEnfants())
                    .hasSize(1)
                    .element(0)
                    .isInstanceOf(NoeudMaquetteEntity.class);
            });

    }

    private NoeudMaquetteEntity newNoeudMaquetteEntity(String code) {
        NoeudMaquetteEntity entity = new NoeudMaquetteEntity();
        entity.setIdDefinition(UUID.randomUUID());
        entity.setCode(code);
        entity.setLibelleCourt("lib-" + code);
        return entity;
    }
}