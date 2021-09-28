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
import com.example.sdn6.repository.FormationRepository;
import com.example.sdn6.repository.NoeudMaquetteRepository;
import com.example.sdn6.repository.NoeudTypeRepository;
import com.example.sdn6.repository.ObjetFormationRepository;
import com.example.sdn6.spa.NoeudMaquetteServicePortAdapter;
import org.junit.jupiter.api.BeforeEach;
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