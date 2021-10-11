package com.example.sdn6;

import java.io.*;
import java.util.List;
import static org.assertj.core.api.Assertions.assertThat;
import com.example.sdn6.containers.Neo4j;
import com.example.sdn6.entity.NoeudMaquetteEntity;
import com.example.sdn6.projection.NoeudAndFormationsParentesInterface;
import com.example.sdn6.projection.NoeudAndFormationsParentesResult;
import com.example.sdn6.repository.NoeudMaquetteRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.neo4j.core.Neo4jClient;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;

@SpringBootTest
class Sdn6Test {

    @Autowired
    private NoeudMaquetteRepository repository;

    @Autowired
    private Neo4jClient neo4jClient;

    @BeforeEach
    void setupData() throws IOException {
        neo4jClient.query("MATCH (n) DETACH DELETE n").run();
        CypherUtils.loadCypherFromResource("/data.cypher", neo4jClient);
    }

    @Test
    void testFindAllNoeudAndFormationsParentesByCode() {

        String code = "OF1";

        List<NoeudAndFormationsParentesResult> noeudAndFormations = repository.findAllNoeudAndFormationsParentesByCode(code);

        assertThat(noeudAndFormations).hasSize(1);
        NoeudAndFormationsParentesResult result = noeudAndFormations.get(0);
        assertThat(result.getOm()).isNotNull();
        assertThat(result.getOm().getCode()).isEqualTo(code);
        assertThat(result.getFormationsParentes()).hasSize(1);
        // issue: relations are not hydrated
        assertThat(result.getOm().getType()).isNotNull();
    }

    @Test
    void testFindAllNoeudByCode() {

        String code = "OF1";

        List<NoeudMaquetteEntity> entities = repository.findAllNoeudByCode(code);

        assertThat(entities).hasSize(1);
        NoeudMaquetteEntity entity = entities.get(0);
        assertThat(entity.getCode()).isEqualTo(code);
        // issue: relations are not hydrated
        assertThat(entity.getType()).isNotNull();
    }

    @Test
    void testFindAllNoeudByCodeWithNeo4jTemplate() {

        String code = "OF1";

        List<NoeudMaquetteEntity> entities = repository.findAllNoeudByCodeWithNeo4jTemplate(code);

        assertThat(entities).hasSize(1);
        NoeudMaquetteEntity entity = entities.get(0);
        assertThat(entity.getCode()).isEqualTo(code);
        // issue: relations are not hydrated
        assertThat(entity.getType()).isNotNull();
    }

    @Test
    void testFindAllNoeudAndFormationsParentesByCodeWithNeo4jTemplate() {

        String code = "OF1";

        List<NoeudAndFormationsParentesResult> noeudAndFormations = repository.findAllNoeudAndFormationsParentesByCodeWithNeo4jTemplate(code);

        assertThat(noeudAndFormations).hasSize(1);
        NoeudAndFormationsParentesResult result = noeudAndFormations.get(0);
        assertThat(result.getOm()).isNotNull();
        assertThat(result.getOm().getCode()).isEqualTo(code);
        assertThat(result.getFormationsParentes()).hasSize(1);
        // issue: relations are not hydrated
        assertThat(result.getOm().getType()).isNotNull();
    }

    @Test
    void testFindAllNoeudAndFormationsParentesByCodeWithQueryMethod() {

        String code = "OF1";

        List<NoeudAndFormationsParentesResult> noeudAndFormations = repository.findAllNoeudAndFormationsParentesByCodeWithQueryMethod(code);

        assertThat(noeudAndFormations).hasSize(1);
        NoeudAndFormationsParentesResult result = noeudAndFormations.get(0);
        assertThat(result.getOm()).isNotNull();
        assertThat(result.getOm().getCode()).isEqualTo(code);
        assertThat(result.getFormationsParentes()).hasSize(1);
        // issue: relations are not hydrated
        assertThat(result.getOm().getType()).isNotNull();
    }

    @Test
    void testFindAllNoeudAndFormationsParentesProjectionInterfaceByCodeWithQueryMethod() {

        String code = "OF1";

        List<NoeudAndFormationsParentesInterface> noeudAndFormations = repository.findAllNoeudAndFormationsParentesByCodeWithQueryMethodAndProjectedInterface(code);

        assertThat(noeudAndFormations).hasSize(1);
        NoeudAndFormationsParentesInterface result = noeudAndFormations.get(0);
        assertThat(result.getOm()).isNotNull();
        assertThat(result.getOm().getCode()).isEqualTo(code);
        assertThat(result.getFormationsParentes()).hasSize(1);
        // issue: relations are not hydrated
        assertThat(result.getOm().getType()).isNotNull();
    }

    @DynamicPropertySource
    static void neo4jProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.data.neo4j.uri", () -> Neo4j.database().getBoltUrl());
    }
}