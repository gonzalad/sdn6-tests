package com.example.sdn6.entity;

import java.util.Comparator;

/**
 * Énumération de tous les codes catégories possibles.
 */

public enum Categorie {
    FORMATION("Formation", 1), GROUPEMENT("Groupement", 2),
    /**
     * Objet organisationnel
     */
    OO("Objet Organisationnel", 3),
    /**
     * Objet temporel théorique
     */
    OTT("Objet Temporel Théorique", 4),
    /**
     * Objet pédagogique
     */
    OP("Objet Pédagogique", 5);

    private String libelleCourt;
    private String libelleLong;
    private int ordre;

    Categorie(String libelle, int ordre) {
        this.libelleCourt = libelle;
        this.libelleLong = libelle;
        this.ordre = ordre;
    }

    public String getCode() {
        return name();
    }

    public String getLibelleCourt() {
        return libelleCourt;
    }

    public String getLibelleLong() {
        return libelleLong;
    }

    public int getOrdre() {
        return ordre;
    }

    public String getLibelle() {
        return getLibelleLong();
    }

    public static final Comparator<Categorie> TRI = Comparator.nullsFirst(Comparator.comparingInt(Categorie::getOrdre));
}
