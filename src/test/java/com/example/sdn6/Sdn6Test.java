package com.example.sdn6;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import static org.assertj.core.api.Assertions.assertThat;
import com.example.sdn6.container.Neo4j;
import org.junit.jupiter.api.Test;
import org.neo4j.driver.Value;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;

@SpringBootTest
class Sdn6Test {

    @Autowired
    private SampleRepository sampleRepository;

    /**
     * When I remove an entry from a composite property
     * <p>
     * Then I expect the entry to be  removed from the database node.
     * <p>
     * But the entry is not removed
     */
    @Test
    void removeEntryInCompositePropertyShouldRemoveEntryInDb() {

        Map composite = new HashMap(Map.ofEntries(
            Map.entry("entry1", "value1"),
            Map.entry("entry2", "value2")
        ));
        SampleEntity entity = createSample(composite);
        assertThat(entity.getComposite()).hasSize(2);
        entity.getComposite().remove("entry1");
        assertThat(entity.getComposite()).hasSize(1);
        sampleRepository.save(entity);

        Optional<SampleEntity> entityInDatabase = sampleRepository.findById(entity.getId());

        assertThat(entityInDatabase).isPresent();
        assertThat(entityInDatabase.orElseThrow().getName()).isEqualTo(entity.getName());
        assertThat(entityInDatabase.orElseThrow().getComposite()).hasSize(1);
    }

    @Test
    void givenCompositePropertyWithStringEntryWhenQueryThenVerifyReturnsStringInstance() {

        Map composite = new HashMap(Map.ofEntries(
            Map.entry("entry1", "value1"),
            Map.entry("entry2", "value2")
        ));
        SampleEntity entity = createSample(composite);
        sampleRepository.save(entity);

        Optional<SampleEntity> entityInDatabase = sampleRepository.findById(entity.getId());

        assertThat(entityInDatabase).isPresent();
        assertThat(entityInDatabase.orElseThrow().getComposite().get("entry2")).isEqualTo("value2");
    }

    /**
     * This test works (just to check basic composite property usage)
     */
    @Test
    void testUpdateEntryInCompositeProperty() {

        Map composite = new HashMap(Map.ofEntries(
            Map.entry("entry1", "value1"),
            Map.entry("entry2", "value2")
        ));
        SampleEntity entity = createSample(composite);
        assertThat(entity.getComposite()).hasSize(2);
        entity.getComposite().put("entry2", "change");
        sampleRepository.save(entity);

        Optional<SampleEntity> entityInDatabase = sampleRepository.findById(entity.getId());

        assertThat(entityInDatabase).isPresent();
        assertThat(entityInDatabase.orElseThrow().getComposite().get("entry2"))
            .extracting(v -> ((Value) v).asString())
            .isEqualTo("change");
    }

    private SampleEntity createSample(Map composite) {
        SampleEntity entity = new SampleEntity();
        entity.setName("sample");
        entity.setComposite(new HashMap<>(composite));
        return sampleRepository.save(entity);
    }

    @DynamicPropertySource
    static void neo4jProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.data.neo4j.uri", () -> Neo4j.database().getBoltUrl());
    }

}