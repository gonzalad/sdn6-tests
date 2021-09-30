package com.example.sdn6.entity.noeud;

import org.springframework.data.neo4j.core.schema.GeneratedValue;
import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.RelationshipProperties;
import org.springframework.data.neo4j.core.schema.TargetNode;

@RelationshipProperties
public class Enfant {

    @GeneratedValue
    @Id
    private Long id;

    @TargetNode
    private NoeudRef enfant;

    private Integer ordre;

    private Boolean obligatoire;

    private Integer min;

    private Integer max;

    Enfant() {
    }

    public Enfant(NoeudRef enfant, boolean obligatoire) {
        this.enfant = enfant;
        this.obligatoire = obligatoire;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public NoeudRef getEnfant() {
        return enfant;
    }

    public void setEnfant(NoeudRef enfant) {
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
