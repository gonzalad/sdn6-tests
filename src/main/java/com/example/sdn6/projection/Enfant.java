package com.example.sdn6.projection;

public class Enfant {

    private NoeudRef enfant;
    private Integer ordre;
    private Boolean obligatoire;

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
}
