package com.example.sdn6.projection;

public class Enfant {

    private NoeudRef enfant;

    private Integer ordre;

    private Boolean obligatoire;

    private Integer min;

    private Integer max;

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
}
