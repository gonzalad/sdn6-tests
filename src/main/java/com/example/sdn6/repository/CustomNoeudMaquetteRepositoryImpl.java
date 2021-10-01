package com.example.sdn6.repository;

import com.example.sdn6.entity.NoeudEntity;
import com.example.sdn6.projection.NoeudProjection;
import org.springframework.data.neo4j.core.Neo4jTemplate;

public class CustomNoeudMaquetteRepositoryImpl implements CustomNoeudMaquetteRepository {

    private final Neo4jTemplate neo4jTemplate;

    public CustomNoeudMaquetteRepositoryImpl(Neo4jTemplate neo4jTemplate) {
        this.neo4jTemplate = neo4jTemplate;
    }

    @Override
    public NoeudProjection save(NoeudProjection projection) {
        return neo4jTemplate.save(NoeudEntity.class).one(projection);
    }
}
