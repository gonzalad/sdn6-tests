package com.example.sdn6;

import java.io.*;
import java.util.Optional;
import static org.assertj.core.api.Assertions.assertThat;
import com.example.sdn6.container.Neo4j;
import com.example.sdn6.entity.WidgetEntity;
import com.example.sdn6.entity.WidgetTypeEntity;
import com.example.sdn6.repository.WidgetRepository;
import com.example.sdn6.repository.WidgetTypeRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.neo4j.core.Neo4jClient;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
class Sdn6Test {

    @Autowired
    private WidgetRepository repository;

    @Autowired
    private WidgetTypeRepository typeRepository;

    @Autowired
    private Neo4jClient neo4jClient;
    private WidgetTypeEntity window;
    private WidgetTypeEntity panel;
    private WidgetTypeEntity button;

    @BeforeEach
    void setUp() throws IOException {
        CypherUtils.loadCypherFromResource("data.cypher", neo4jClient);
        window = findType("Window");
        panel = findType("Panel");
        button = findType("Button");
    }

    @Test
    void testFindById() {

        Optional<WidgetEntity> window = repository.findByCode("Window1");

        assertThat(window).isPresent();
    }

    @Test
    void testCreateSingleWidget() {

        WidgetEntity w3 = newWidget("w3", window);

        repository.save(w3);
    }

    @Test
    void testCreateWidgetTree() {

        WidgetEntity b3 = newWidget("b3", button);
        WidgetEntity p3 = newWidget("p3", panel).child(b3);
        WidgetEntity w3 = newWidget("w3", window).child(p3);

        repository.save(w3);
    }

    private WidgetEntity newWidget(String code, WidgetTypeEntity type) {
        WidgetEntity widget = new WidgetEntity();
        widget.setCode(code);
        widget.setType(type);
        return widget;
    }

    private WidgetTypeEntity findType(String code) {
        return typeRepository.findByCode(code).orElseThrow();
    }

    @DynamicPropertySource
    static void neo4jProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.data.neo4j.uri", () -> Neo4j.database().getBoltUrl());
    }

}