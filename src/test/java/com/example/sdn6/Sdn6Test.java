package com.example.sdn6;

import java.io.*;
import java.util.List;
import java.util.Optional;
import static org.assertj.core.api.Assertions.assertThat;
import com.example.sdn6.container.Neo4j;
import com.example.sdn6.entity.Child;
import com.example.sdn6.entity.WidgetAndChilds;
import com.example.sdn6.entity.WidgetEntity;
import com.example.sdn6.entity.WidgetSummary;
import com.example.sdn6.entity.WidgetTypeEntity;
import com.example.sdn6.repository.WidgetRepository;
import com.example.sdn6.repository.WidgetTypeRepository;
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
    void testFindByCode() {

        Optional<WidgetEntity> window = repository.findByCode("Window1");

        assertThat(window).isPresent();
    }

    @Test
    void testCreateSingleWidget() {

        WidgetEntity w3 = newWidget("w3", window);

        WidgetEntity saved = repository.save(w3);

        assertThat(saved).isNotNull();
    }

    @Test
    void testCreateWidgetTree() {

        WidgetEntity b3 = newWidget("b3", button);
        WidgetEntity p3 = newWidget("p3", panel).child(b3);
        WidgetEntity w3 = newWidget("w3", window).child(p3);

        WidgetEntity saved = repository.save(w3);

        assertThat(saved).isNotNull();
    }

    @Test
    void testFindSummaryByCode() {

        String code = "Window1";

        Optional<WidgetSummary> window = repository.findSummaryByCode(code);

        assertThat(window).isPresent();
        assertThat(window.orElseThrow().getCode()).isEqualTo(code);
    }

    @Test
    void testSaveAsSummary() {

        String code = "Window1";
        WidgetEntity window = repository.findByCode(code).orElseThrow();
        window.setLabel("changed");
        window.setChilds(List.of());

        template.saveAs(window, WidgetSummary.class);

        window = repository.findByCode(code).orElseThrow();
        assertThat(window.getLabel()).isEqualTo("changed");
    }

    @Test
    void testSaveAsWidgetAndChild() {

        String code = "Window1";
        WidgetEntity window = repository.findByCode(code).orElseThrow();
        window.setLabel("changed");
        WidgetEntity b2 = repository.findByCode("Button2").orElseThrow();
        window.addChild(b2);
        // b2.label should not change
        b2.setLabel("changed");

        template.saveAs(window, WidgetAndChilds.class);

        window = repository.findByCode(code).orElseThrow();
        assertThat(window.getLabel()).isEqualTo("changed");
        assertThat(window.getChilds()).hasSize(2);
        assertThat(window.getChilds())
            .filteredOn(c -> c.getTarget().getCode().equals("Button2"))
            .extracting(Child::getTarget)
            .hasSize(1)
            .element(0)
            .extracting(WidgetEntity::getLabel)
            .isNull();
    }

    @Test
    void testSaveAsWidgetAndChildWithCompositeProperty() {

        String code = "Window1";
        WidgetEntity window = repository.findByCode(code).orElseThrow();
        window.setLabel("changed");
        WidgetEntity b2 = repository.findByCode("Button2").orElseThrow();
        window.addChild(b2);
        window.getAdditionalFields().put("key1", "value1");

        template.saveAs(window, WidgetAndChilds.class);

        window = repository.findByCode(code).orElseThrow();
        assertThat(window.getLabel()).isEqualTo("changed");
        assertThat(window.getAdditionalFields()).hasSize(1);
    }

    @Test
    void testEntityPartiallyLoaded() {

        String code = "Window1";

        WidgetEntity window = repository.findWithChildrenByCode(code).orElseThrow();

        assertThat(window.getType()).isNotNull();
        assertThat(window.getCode()).isEqualTo("Window1");
        assertThat(window.getChilds()).hasSize(1);
        WidgetEntity child = window.getChilds().get(0).getTarget();
        assertThat(child.getCode()).isEqualTo("Panel1");
        assertThat(child).isNotNull();
        assertThat(child.getType()).isNull();
        assertThat(child.getChilds()).isEmpty();
    }

    @Test
    void givenEntityPartiallyLoadedWhenSaveAsWidgetAndChildThenCheckResults() {

        String code = "Window1";
        WidgetEntity window = repository.findWithChildrenByCode(code).orElseThrow();
        window.setLabel("changed");
        WidgetEntity b2 = repository.findByCode("Button2").orElseThrow();
        window.addChild(b2);
        // b2.label should not change
        b2.setLabel("changed");

        template.saveAs(window, WidgetAndChilds.class);

        window = repository.findByCode(code).orElseThrow();
        assertThat(window.getLabel()).isEqualTo("changed");
        assertThat(window.getChilds()).hasSize(2);
        assertThat(window.getChilds())
            .filteredOn(c -> c.getTarget().getCode().equals("Button2"))
            .extracting(Child::getTarget)
            .hasSize(1)
            .element(0)
            .extracting(WidgetEntity::getLabel)
            .isNull();
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