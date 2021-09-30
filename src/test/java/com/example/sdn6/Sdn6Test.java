package com.example.sdn6;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import static org.assertj.core.api.Assertions.assertThat;
import com.example.sdn6.entity.Categorie;
import com.example.sdn6.entity.ChampAdditionnelEntity;
import com.example.sdn6.entity.FormationEntity;
import com.example.sdn6.entity.NoeudMaquetteAPourEnfantRelationEntity;
import com.example.sdn6.entity.NoeudMaquetteEntity;
import com.example.sdn6.entity.NoeudTypeEntity;
import com.example.sdn6.entity.ObjetFormationEntity;
import com.example.sdn6.entity.TypeChampAdditionnel;
import com.example.sdn6.entity.noeud.NoeudViewEntity;
import com.example.sdn6.repository.FormationRepository;
import com.example.sdn6.repository.NoeudMaquetteRepository;
import com.example.sdn6.repository.NoeudProjectionRepository;
import com.example.sdn6.repository.NoeudTypeRepository;
import com.example.sdn6.repository.ObjetFormationRepository;
import com.example.sdn6.spa.NoeudMaquetteServicePortAdapter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class Sdn6Test {

    @Autowired
    private ObjetFormationRepository objetFormationRepository;

    @Autowired
    private FormationRepository formationRepository;

    @Autowired
    private NoeudMaquetteRepository noeudMaquetteRepository;

    @Autowired
    private NoeudTypeRepository noeudTypeRepository;

    @Autowired
    private NoeudMaquetteServicePortAdapter spa;

    @Autowired
    private NoeudProjectionRepository noeudProjectionRepository;

    private NoeudTypeEntity formationType;
    private NoeudTypeEntity parcoursType;

    @BeforeEach
    void setUp() {
        objetFormationRepository.deleteAll();
        formationRepository.deleteAll();
        noeudTypeRepository.deleteAll();
        formationType = newType("FORMATION", Categorie.FORMATION);
        parcoursType = newType("PT", Categorie.OO);
        noeudTypeRepository.saveAll(List.of(
            formationType,
            parcoursType
        ));
    }

    @Test
    void createOf() {
        List<ObjetFormationEntity> entities = List.of(
            newObjetFormation("OF1"),
            newObjetFormation("OF2")
        );
        objetFormationRepository.saveAll(entities);
    }

    @Test
    void createOfWithEnfant() {
        ObjetFormationEntity of1 = newObjetFormation("OF1");
        of1.addEnfant(newObjetFormation("OF2"), true);
        List<ObjetFormationEntity> entities = List.of(
            of1
        );
        objetFormationRepository.saveAll(entities);
    }

    @Test
    void createFormationWithEnfant() {
        FormationEntity f1 = newFormation("F1");
        f1.addEnfant(newObjetFormation("OF1"), true);
        List<FormationEntity> entities = List.of(
            f1
        );
        formationRepository.saveAll(entities);
    }

    @Test
    void lireArbre() {
        FormationEntity f1 = newFormation("F1");
        ObjetFormationEntity of1 = newObjetFormation("OF1");
        ObjetFormationEntity of2 = newObjetFormation("OF2");
        f1.addEnfant(of1, true);
        f1.addEnfant(of2, true);
        FormationEntity f2 = newFormation("F2");
        List<FormationEntity> entities = List.of(
            f1,
            f2
        );
        formationRepository.saveAll(entities);

        Optional<NoeudMaquetteEntity> f1Lue = spa.lireNoeudAvecDescendance(f1.getIdDefinition());

        assertThat(f1Lue).isNotEmpty();
        assertThat(f1Lue.get().getCode()).isEqualTo(f1.getCode());
        assertThat(f1Lue.get().getEnfants()).hasSize(2);
        assertThat(f1Lue.get().getEnfants())
            .extracting(NoeudMaquetteAPourEnfantRelationEntity::getEnfant)
            .extracting(NoeudMaquetteEntity::getCode)
            .containsExactlyInAnyOrder(of1.getCode(), of2.getCode());
    }

    @Test
    void lireArbreAvecObjetsDupliques() {
        FormationEntity f1 = newFormation("F1");
        ObjetFormationEntity of2 = newObjetFormation("OF2");
        ObjetFormationEntity of1 = newObjetFormation("OF1");
        of1.addEnfant(of2, true);
        f1.addEnfant(of1, true);
        f1.addEnfant(of2, true);
        List<NoeudMaquetteEntity> entities = List.of(
            of2,
            of1,
            f1
        );

        noeudMaquetteRepository.saveAll(entities);
        /*noeudMaquetteRepository.save(f1);
        noeudMaquetteRepository.save(of1);
        noeudMaquetteRepository.save(of2);*/

        Optional<NoeudMaquetteEntity> f1Lue = spa.lireNoeudAvecDescendance(f1.getIdDefinition());
        assertThat(f1Lue).isNotEmpty();
        assertThat(f1Lue.get().getCode()).isEqualTo(f1.getCode());
        assertThat(f1Lue.get().getEnfants())
            .hasSize(2)
            .extracting(NoeudMaquetteAPourEnfantRelationEntity::getEnfant)
            .extracting(NoeudMaquetteEntity::getCode)
            .containsExactlyInAnyOrder(of1.getCode(), of2.getCode());
        assertThat(f1Lue.get().getEnfants())
            .extracting(NoeudMaquetteAPourEnfantRelationEntity::getEnfant)
            .filteredOn(om -> of1.getCode().equals(om.getCode()))
            .flatExtracting(NoeudMaquetteEntity::getEnfants)
            .hasSize(1)
            .element(0)
            .extracting(NoeudMaquetteAPourEnfantRelationEntity::getEnfant)
            .extracting(NoeudMaquetteEntity::getCode)
            .isEqualTo(of2.getCode());
    }

    /**
     * Vérifie si des relations sont supprimées après une sauvegarde d'un graphe complet
     */
    @Test
    void whenPersistGraphThenVerifyRelationsInGraphArePreserved() {
        FormationEntity f1 = newFormation("F1");
        ObjetFormationEntity of2 = newObjetFormation("OF2");
        ObjetFormationEntity of1 = newObjetFormation("OF1");
        ObjetFormationEntity of11 = newObjetFormation("OF11");
        of1.addEnfant(of11, true);
        f1.addEnfant(of1, true);
        f1.addEnfant(of2, true);
        List<NoeudMaquetteEntity> entities = List.of(
            of2,
            of1,
            of11,
            f1
        );
        noeudMaquetteRepository.saveAll(entities);
        Optional<NoeudMaquetteEntity> f1Lue = spa.lireNoeudAvecDescendance(f1.getIdDefinition());
        assertThat(f1Lue).isNotEmpty();
        NoeudMaquetteEntity of1AModifier = f1Lue.get().getEnfants().stream()
            .map(NoeudMaquetteAPourEnfantRelationEntity::getEnfant)
            .filter(om -> om.getCode().equals("OF1"))
            .findAny().orElseThrow();
        of1AModifier.setLibelleCourt("modifie");
        noeudMaquetteRepository.save(of1AModifier);

        f1Lue = spa.lireNoeudAvecDescendance(f1.getIdDefinition());

        assertThat(f1Lue.get().getCode()).isEqualTo(f1.getCode());
        assertThat(f1Lue.get().getEnfants())
            .hasSize(2)
            .extracting(NoeudMaquetteAPourEnfantRelationEntity::getEnfant)
            .extracting(NoeudMaquetteEntity::getCode)
            .containsExactlyInAnyOrder(of1.getCode(), of2.getCode());
        assertThat(f1Lue.get().getEnfants())
            .extracting(NoeudMaquetteAPourEnfantRelationEntity::getEnfant)
            .filteredOn(om -> of1.getCode().equals(om.getCode()))
            .hasSize(1)
            .element(0)
            .satisfies(om -> {
                assertThat(om.getLibelleCourt()).isEqualTo("modifie");
                assertThat(om.getEnfants()).hasSize(1);
                assertThat(om.getEnfants().get(0).getEnfant().getCode()).isEqualTo("OF11");
            });
    }

    /**
     * Montre le soucis: si on load et qu'on persiste juste un noeud, alors on perd des relations
     */
    @Test
    void whenPersistNoeudThenVerifyRelationsInGraphWhereLost() {

        // 1. Création graphe initial
        FormationEntity f1 = newFormation("F1");
        ObjetFormationEntity of2 = newObjetFormation("OF2");
        ObjetFormationEntity of1 = newObjetFormation("OF1");
        ObjetFormationEntity of11 = newObjetFormation("OF11");
        of1.addEnfant(of11, true);
        f1.addEnfant(of1, true);
        f1.addEnfant(of2, true);
        List<NoeudMaquetteEntity> entities = List.of(
            of2,
            of1,
            of11,
            f1
        );
        noeudMaquetteRepository.saveAll(entities);

        // 2. Récupération d'un noeud et persistance du noeud
        Optional<NoeudMaquetteEntity> of1AModifier = spa.lireNoeud(of1.getIdDefinition());
        assertThat(of1AModifier).isNotEmpty();
        assertThat(of1AModifier.get().getCode()).isEqualTo("OF1");
        of1AModifier.get().setLibelleCourt("modifie");
        noeudMaquetteRepository.save(of1AModifier.get());

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
            .filteredOn(om -> om.getCode().equals(of1AModifier.get().getCode()))
            .element(0)
            .satisfies(om -> {
                assertThat(om.getLibelleCourt()).isEqualTo("modifie");
                // la relation (of1)-[:HAS_ENFANT]->(of11) a été supprimée :(
                assertThat(om.getEnfants()).hasSize(0);
            });
    }

    /**
     * Montre le soucis: si on load et qu'on persiste juste un noeud, alors on perd des relations
     * <p>
     * Ne fonctionne pas, car on a au démarrage org.springframework.data.mapping.MappingException: The schema already contains a node description under the primary label ObjetMaquette
     */
    @Test
    @Disabled
    void whenPersistViewThenVerifyRelationsInGraphArePreserved() {

        // 1. Création graphe initial
        FormationEntity f1 = newFormation("F1");
        ObjetFormationEntity of2 = newObjetFormation("OF2");
        ObjetFormationEntity of1 = newObjetFormation("OF1");
        of1.setLibelleLong("libelleLong");
        ObjetFormationEntity of11 = newObjetFormation("OF11");
        of1.addEnfant(of11, true);
        f1.addEnfant(of1, true);
        f1.addEnfant(of2, true);
        List<NoeudMaquetteEntity> entities = List.of(
            of2,
            of1,
            of11,
            f1
        );
        noeudMaquetteRepository.saveAll(entities);

        // 2. Récupération d'un noeud et persistance du noeud
        Optional<NoeudViewEntity> of1AModifier = spa.lireNoeudProjection(of1.getIdDefinition());
        assertThat(of1AModifier).isNotEmpty();
        assertThat(of1AModifier.get().getCode()).isEqualTo("OF1");
        of1AModifier.get().setLibelleCourt("modifie");
        noeudProjectionRepository.save(of1AModifier.get());

        // 3. Vérification des relations de la formation
        Optional<NoeudMaquetteEntity> f1Lue = spa.lireNoeudAvecDescendance(f1.getIdDefinition());
        assertThat(f1Lue)
            .get()
            .isInstanceOf(FormationEntity.class);
        assertThat(f1Lue.get().getEnfants())
            .hasSize(2)
            .extracting(NoeudMaquetteAPourEnfantRelationEntity::getEnfant)
            .extracting(NoeudMaquetteEntity::getCode)
            .containsExactlyInAnyOrder(of1.getCode(), of2.getCode());
        assertThat(f1Lue.get().getEnfants())
            .extracting(NoeudMaquetteAPourEnfantRelationEntity::getEnfant)
            .filteredOn(om -> om.getCode().equals(of1AModifier.get().getCode()))
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
                    .isInstanceOf(ObjetFormationEntity.class);
            });
    }

    private ObjetFormationEntity newObjetFormation(String code) {
        ObjetFormationEntity entity = new ObjetFormationEntity();
        entity.setType(parcoursType);
        return setupObjetMaquette(code, entity);
    }

    private FormationEntity newFormation(String code) {
        FormationEntity entity = new FormationEntity();
        entity.setType(formationType);
        return setupObjetMaquette(code, entity);
    }

    private NoeudTypeEntity newType(String code, Categorie categorie) {
        NoeudTypeEntity entity = new NoeudTypeEntity();
        entity.setCategorie(categorie);
        entity.setCode(code);
        entity.setDateFin(LocalDate.now());
        ChampAdditionnelEntity champAdditionnel = newChampsAdditionnel("CHAMP1");
        entity.setChampsAdditionnels(List.of(champAdditionnel));
        return entity;
    }

    private ChampAdditionnelEntity newChampsAdditionnel(String codeChamps) {
        ChampAdditionnelEntity champAdditionnel = new ChampAdditionnelEntity();
        champAdditionnel.setCode(codeChamps);
        champAdditionnel.setType(TypeChampAdditionnel.VALEURBOOLEENNE);
        return champAdditionnel;
    }

    private <E extends NoeudMaquetteEntity> E setupObjetMaquette(String code, E entity) {
        entity.setIdDefinition(UUID.randomUUID());
        entity.setCode(code);
        entity.setLibelleCourt("lib-" + code);
        return entity;
    }
}