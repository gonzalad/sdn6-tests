package com.example.sdn6;

import java.time.LocalDate;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;
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
import org.neo4j.cypherdsl.core.renderer.Renderer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.annotation.ReadOnlyProperty;
import org.springframework.data.mapping.AssociationHandler;
import org.springframework.data.mapping.PersistentPropertyAccessor;
import org.springframework.data.neo4j.core.Neo4jClient;
import org.springframework.data.neo4j.core.mapping.Constants;
import org.springframework.data.neo4j.core.mapping.CreateRelationshipStatementHolder;
import org.springframework.data.neo4j.core.mapping.MappingSupport;
import org.springframework.data.neo4j.core.mapping.Neo4jMappingContext;
import org.springframework.data.neo4j.core.mapping.Neo4jPersistentEntity;
import org.springframework.data.neo4j.core.mapping.Neo4jPersistentProperty;
import org.springframework.data.neo4j.core.mapping.NestedRelationshipContext;
import org.springframework.data.neo4j.core.mapping.NodeDescription;

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

    @Autowired
    private Neo4jMappingContext schema;

    private static final Renderer renderer = Renderer.getDefaultRenderer();

    @Autowired
    private Neo4jClient neo4jClient;

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

    // 1. sauvegarder uniquement descripteur
    NoeudMaquetteEntity save(NoeudMaquetteEntity entity) {

        var nodeDescription = schema.getRequiredNodeDescription(entity.getClass());
        NoeudMaquetteEntity saved = createOrUpdateNode(entity, nodeDescription);
        Neo4jPersistentEntity persistentEntity = (Neo4jPersistentEntity) nodeDescription;
        boolean isNew = persistentEntity.isNew(entity);
        entity.setId(saved.getId());
        Object fromId = saved.getId();

        schema.getPersistentEntity(entity.getClass()).doWithAssociations((AssociationHandler<Neo4jPersistentProperty>) association -> {
            // code récupéré en grande partie de Neo4jTemplate#processNestedRelations
            if (association.getInverse().isAnnotationPresent(ReadOnlyProperty.class)) {
                return;
            }
            PersistentPropertyAccessor<NoeudMaquetteEntity> parentPropertyAccessor = persistentEntity.getPropertyAccessor(entity);
            if (isNew) {
                // creates all relations (only first level) - see CypherGenerator#prepareSaveOfRelationshipWithProperties
                NestedRelationshipContext relationshipContext = NestedRelationshipContext.of(association, parentPropertyAccessor, persistentEntity);
                Object rawValue = relationshipContext.getValue();
                Collection<?> relatedValuesToStore = unifyRelationshipValue(rawValue);
                for (Object relatedValue : relatedValuesToStore) {
                    createRelation(persistentEntity, relationshipContext, fromId, relatedValue);
                }
            } else {
                // on ne traite que les relations enfants (les autres relations sont initialisées uniquement durant la création)
                // - supprimer les relations enfants
                // - recréer les relations enfants
                if (!association.getInverse().getFieldName().equals("enfants")) {
                    return;
                }
                NestedRelationshipContext relationshipContext = NestedRelationshipContext.of(association, parentPropertyAccessor, persistentEntity);
                removeRelations(relationshipContext, persistentEntity);
                Object rawValue = relationshipContext.getValue();
                Collection<?> relatedValuesToStore = unifyRelationshipValue(rawValue);
                // Object fromId = relationshipContext.getRelationshipPropertiesPropertyAccessor(persistentEntity.getIdProperty());
                for (Object relatedValue : relatedValuesToStore) {
                    createRelation(persistentEntity, relationshipContext, fromId, relatedValue);
                }
            }
        });

        return saved;
    }

    private void createRelation(Neo4jPersistentEntity persistentEntity, NestedRelationshipContext relationshipContext, Object fromId,
                           Object relatedValue) {
        String relationName = relationshipContext.getRelationship().getType();
        CreateRelationshipStatementHolder statementHolder = schema.createStatement(
            persistentEntity, relationshipContext, relatedValue, true);
        Object relatedEntity = relationshipContext
            .identifyAndExtractRelationshipTargetNode(relatedValue);
        Neo4jPersistentEntity<?> targetEntity = schema.getPersistentEntity(relatedEntity.getClass());
        Object toId = targetEntity.getPropertyAccessor(relatedEntity).getProperty(targetEntity.getIdProperty());
        if (toId == null) {
            throw new IllegalStateException("Entité " + targetEntity + " doit être persistée avant de créer des relations");
        }
        Optional<Long> relationshipInternalId = neo4jClient.query(renderer.render(statementHolder.getStatement()))
            .bind(fromId) //
            .to(Constants.FROM_ID_PARAMETER_NAME) //
            .bind(toId) //
            .to(Constants.TO_ID_PARAMETER_NAME) //
            .bind(relationName) //
            .to(Constants.NAME_OF_KNOWN_RELATIONSHIP_PARAM) //
            .bindAll(statementHolder.getProperties())
            .fetchAs(Long.class).one();
    }

    private void removeRelations(NestedRelationshipContext relationshipContext,
                                 Neo4jPersistentEntity persistentEntity) { //Neo4jPersistentEntity persistentEntity, PersistentPropertyAccessor<NoeudMaquetteEntity> parentPropertyAccessor, String relationName) {
        String relationName = relationshipContext.getRelationship().getType();
        Object fromId = relationshipContext.getRelationshipPropertiesPropertyAccessor(persistentEntity.getIdProperty());
        StringBuilder queryBuilder = new StringBuilder("MATCH (startNode)-[rel:`").append(relationName).append("`]->()").append(System.lineSeparator());
        queryBuilder.append("DELETE rel").append(System.lineSeparator());
        // Object fromId = parentPropertyAccessor.getProperty(persistentEntity.getRequiredIdProperty());
        neo4jClient.query(queryBuilder.toString())
            .bind(fromId) //
            .to(Constants.FROM_ID_PARAMETER_NAME) //
            .run();
    }

    private NoeudMaquetteEntity createOrUpdateNode(NoeudMaquetteEntity entity, NodeDescription<?> nodeDescription) {
        var labels = labels(nodeDescription);
        StringBuilder queryBuilder = new StringBuilder("OPTIONAL MATCH (hlp:").append(labels).append(")").append(System.lineSeparator());
        queryBuilder.append("WHERE id(hlp) = $__id__").append(System.lineSeparator());
        queryBuilder.append("WITH hlp WHERE hlp IS NULL").append(System.lineSeparator());
        queryBuilder.append("CREATE (n:").append(labels).append(")").append(System.lineSeparator());
        queryBuilder.append("SET n = $__properties__ ").append(System.lineSeparator());
        queryBuilder.append("RETURN n").append(System.lineSeparator());
        queryBuilder.append("UNION MATCH (n:").append(labels).append(")").append(System.lineSeparator());
        queryBuilder.append("WHERE id(n) = $__id__").append(System.lineSeparator());
        queryBuilder.append("SET n += $__properties__ RETURN n").append(System.lineSeparator());

        var binder = schema.getRequiredBinderFunctionFor(NoeudMaquetteEntity.class);
        var mappingFunction = schema.getRequiredMappingFunctionFor(entity.getClass());
        NoeudMaquetteEntity saved = neo4jClient.query(queryBuilder.toString())
            .bindAll(binder.apply(entity))
            .fetchAs(NoeudMaquetteEntity.class)
            .mappedBy((typeSystem, record) -> {
                return mappingFunction.apply(typeSystem, record.get("n"));
            }).one().orElseThrow();
        return saved;
    }

    private Collection<?> unifyRelationshipValue(Object rawValue) {
        if (rawValue == null) {
            return Collections.emptySet();
        }
        Collection<?> result = rawValue instanceof Collection ? (Collection<?>) rawValue : Collections.emptySet();
        return result;
//        return result.stream().map(o ->
//            o instanceof MappingSupport.RelationshipPropertiesWithEntityHolder ? ((MappingSupport.RelationshipPropertiesWithEntityHolder) o).getRelatedEntity() : o
//        ).collect(Collectors.toList());
    }

    private String labels(NodeDescription<?> nodeDescription) {
        StringBuilder queryBuilder = new StringBuilder("`").append(nodeDescription.getPrimaryLabel()).append('`');
        for (String additionalLabel : nodeDescription.getAdditionalLabels()) {
            queryBuilder.append(":`").append(additionalLabel).append('`');
        }
        return queryBuilder.toString();
    }

    // 2. sauvegarder descripteur et relations enfants
    void saveWithRelationsEnfants(NoeudMaquetteEntity entity) {
    }

    // 3. sauvegarder descripteurs et relations enfants
    // attention à bien gérer les dépendances entre entités (i.e. objetMaquette qui n'existent pas encore)
    void saveWithRelationsEnfants(List<NoeudMaquetteEntity> entity) {
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
        save(f1);
        var binder = schema.getRequiredBinderFunctionFor(NoeudMaquetteEntity.class);
        var nodeDescription = schema.getRequiredNodeDescription(f1.getClass());
        System.out.println(nodeDescription.getPrimaryLabel());
        System.out.println(nodeDescription.getAdditionalLabels());
        System.out.println(nodeDescription.getGraphProperties());
        Map<String, Object> binded = binder.apply(f1);
//        neo4jClient.query("bla")
//                .bind(f1)
//                    .with(f -> {
//                    })

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

    @Test
    void persistManually() {
        // 1. Création graphe initial
        FormationEntity f1 = newFormation("F1");
        ObjetFormationEntity of2 = newObjetFormation("OF2");
        ObjetFormationEntity of1 = newObjetFormation("OF1");
        ObjetFormationEntity of11 = newObjetFormation("OF11");
        of1.addEnfant(of11, true);
        f1.addEnfant(of1, true);
        f1.addEnfant(of2, true);
        save(of11);
        save(of2);
        save(of1);
        save(f1);
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