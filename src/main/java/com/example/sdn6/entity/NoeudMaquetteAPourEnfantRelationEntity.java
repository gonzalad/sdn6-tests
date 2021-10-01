package com.example.sdn6.entity;

import org.springframework.data.neo4j.core.schema.GeneratedValue;
import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.RelationshipProperties;
import org.springframework.data.neo4j.core.schema.TargetNode;

@RelationshipProperties
public class NoeudMaquetteAPourEnfantRelationEntity {

    @GeneratedValue
    @Id
    private Long id;

    @TargetNode
    private NoeudMaquetteEntity enfant;

    private Boolean obligatoire;

    NoeudMaquetteAPourEnfantRelationEntity() {
    }

    public NoeudMaquetteAPourEnfantRelationEntity(NoeudMaquetteEntity enfant, boolean obligatoire) {
        this.enfant = enfant;
        this.obligatoire = obligatoire;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public NoeudMaquetteEntity getEnfant() {
        return enfant;
    }

    public void setEnfant(NoeudMaquetteEntity enfant) {
        this.enfant = enfant;
    }
/*
    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        NoeudMaquetteAPourEnfantRelationEntity that = (NoeudMaquetteAPourEnfantRelationEntity) o;
        return Objects.equals(enfant, that.enfant);
    }

    @Override
    public int hashCode() {
        return Objects.hash(enfant);
    }*/
}
