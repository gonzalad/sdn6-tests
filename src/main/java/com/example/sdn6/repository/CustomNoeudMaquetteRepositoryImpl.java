package com.example.sdn6.repository;

import com.example.sdn6.entity.NoeudMaquetteEntity;
import com.example.sdn6.entity.ObjetFormationEntity;
import com.example.sdn6.projection.NoeudProjection;
import org.springframework.data.neo4j.core.Neo4jTemplate;

public class CustomNoeudMaquetteRepositoryImpl implements CustomNoeudMaquetteRepository {

    private final Neo4jTemplate neo4jTemplate;

    public CustomNoeudMaquetteRepositoryImpl(Neo4jTemplate neo4jTemplate) {
        this.neo4jTemplate = neo4jTemplate;
    }

    @Override
    public NoeudProjection save(NoeudProjection projection) {
        // TODO: pb pour gérer le polymorphisme (passer NoeudMaquetteEntity ne fonctionne pas)
        return neo4jTemplate.save(ObjetFormationEntity.class).one(projection);
    }
}
