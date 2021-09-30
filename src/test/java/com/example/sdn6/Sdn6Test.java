package com.example.sdn6;

import java.util.Optional;
import java.util.UUID;
import static org.assertj.core.api.Assertions.assertThat;
import com.example.sdn6.entity.NoeudMaquetteEntity;
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
        NoeudMaquetteEntity om = newEntity("F1");
        om.setReadOnly(true);
        repository.save(om);

        // use projection for persistence
        Optional<NoeudProjection> projectionAModifier = repository.findProjectionByIdDefinition(om.getIdDefinition());
        assertThat(projectionAModifier).isNotEmpty();
        assertThat(projectionAModifier.get().getCode()).isEqualTo("F1");
        projectionAModifier.get().setCode("modifie");
        repository.save(projectionAModifier.get());

        Optional<NoeudMaquetteEntity> omRead = repository.findByIdDefinition(om.getIdDefinition());
        assertThat(omRead).isPresent();
        // check attribute updated by projection
        assertThat(omRead.get().getCode()).isEqualTo("modifie");
        // check attribute was not updated since it's not part of the projection
        assertThat(omRead.get().isReadOnly()).isEqualTo(true);
    }

    private NoeudMaquetteEntity newEntity(String code) {
        NoeudMaquetteEntity entity = new NoeudMaquetteEntity();
        entity.setIdDefinition(UUID.randomUUID());
        entity.setCode(code);
        return entity;
    }
}