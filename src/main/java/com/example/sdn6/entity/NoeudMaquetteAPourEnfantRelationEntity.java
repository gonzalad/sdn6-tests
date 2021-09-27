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

    private Integer ordre;

    private Boolean obligatoire;

    private Integer min;

    private Integer max;

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

    public Integer getOrdre() {
        return ordre;
    }

    public void setOrdre(Integer ordre) {
        this.ordre = ordre;
    }

    public Boolean getObligatoire() {
        return obligatoire;
    }

    public void setObligatoire(Boolean obligatoire) {
        this.obligatoire = obligatoire;
    }

    public Integer getMin() {
        return min;
    }

    public void setMin(Integer min) {
        this.min = min;
    }

    public Integer getMax() {
        return max;
    }

    public void setMax(Integer max) {
        this.max = max;
    }
}
