package com.example.sdn6;

import java.io.*;
import static org.assertj.core.api.Assertions.assertThat;
import com.example.sdn6.container.Neo4j;
import com.example.sdn6.entity.WidgetProjection;
import com.example.sdn6.entity.WidgetEntity;
import com.example.sdn6.repository.WidgetRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.neo4j.core.Neo4jClient;
import org.springframework.data.neo4j.core.Neo4jTemplate;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
class Sdn6Test {

    @Autowired
    private WidgetRepository repository;

    @Autowired
    private Neo4jTemplate template;

    @Autowired
    private Neo4jClient neo4jClient;

    @BeforeEach
    void setUp() throws IOException {
        CypherUtils.loadCypherFromResource("data.cypher", neo4jClient);
    }

    @Test
    void testSaveAsWidgetProjectionWithCompositeProperty() {

        String code = "Window1";
        WidgetEntity window = repository.findByCode(code).orElseThrow();
        window.setLabel("changed");
        window.getAdditionalFields().put("key1", "value1");

        template.saveAs(window, WidgetProjection.class);

        window = repository.findByCode(code).orElseThrow();
        assertThat(window.getLabel()).isEqualTo("changed");
        assertThat(window.getAdditionalFields()).hasSize(1);
    }


    @DynamicPropertySource
    static void neo4jProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.data.neo4j.uri", () -> Neo4j.database().getBoltUrl());
    }

}