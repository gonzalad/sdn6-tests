package com.example.sdn6;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import static org.assertj.core.api.Assertions.assertThat;
import com.example.sdn6.entity.NoeudEntity;
import com.example.sdn6.projection.NoeudProjection;
import com.example.sdn6.projection.NoeudRef;
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
        NoeudEntity f1 = newNoeudEntity("F1");
        NoeudEntity of2 = newNoeudEntity("OF2");
        NoeudEntity of1 = newNoeudEntity("OF1");
        of1.setLastname("libelleLong");
        NoeudEntity of11 = newNoeudEntity("OF11");
        of1.setChild(of11);
        f1.setChild(of1);
        List<NoeudEntity> entities = List.of(
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
        assertThat(projectionAModifier.get().getChild()).isInstanceOf(NoeudRef.class);
        projectionAModifier.get().setFirstname("modifie");
        repository.save(projectionAModifier.get());

        // 3. Vérification des relations de la formation
        Optional<NoeudEntity> f1Lue = spa.lireNoeudAvecDescendance(f1.getIdDefinition());
        assertThat(f1Lue).isPresent();
        assertThat(f1Lue.get().getChild().getCode()).isEqualTo(of1.getCode());
        assertThat(f1Lue.get().getChild())
            .satisfies(om -> {
                assertThat(om.getFirstName()).isEqualTo("modifie");
                assertThat(om.getLastName()).isEqualTo("libelleLong");
                assertThat(om.getChild()).isInstanceOf(NoeudEntity.class);
            });

    }

    private NoeudEntity newNoeudEntity(String code) {
        NoeudEntity entity = new NoeudEntity();
        entity.setIdDefinition(UUID.randomUUID());
        entity.setCode(code);
        entity.setFirstName("lib-" + code);
        return entity;
    }
}