package com.example.sdn6.entity;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import org.springframework.data.neo4j.core.schema.GeneratedValue;
import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.Node;
import org.springframework.data.neo4j.core.schema.Relationship;

@Node("NoeudType")
public class NoeudTypeEntity {

    @GeneratedValue
    @Id
    private Long id;
    private String code;
    private String codeStructure;
    private Categorie categorie;
    private LocalDate dateDebut;
    private LocalDate dateFin;

    @Relationship(type = "A_POUR_CHAMP_ADDITIONNEL")
    private List<ChampAdditionnelEntity> champsAdditionnels = new ArrayList<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getCodeStructure() {
        return codeStructure;
    }

    public void setCodeStructure(String codeStructure) {
        this.codeStructure = codeStructure;
    }

    public Categorie getCategorie() {
        return categorie;
    }

    public void setCategorie(Categorie categorie) {
        this.categorie = categorie;
    }

    public LocalDate getDateDebut() {
        return dateDebut;
    }

    public void setDateDebut(LocalDate dateDebut) {
        this.dateDebut = dateDebut;
    }

    public LocalDate getDateFin() {
        return dateFin;
    }

    public void setDateFin(LocalDate dateFin) {
        this.dateFin = dateFin;
    }

    public List<ChampAdditionnelEntity> getChampsAdditionnels() {
        return champsAdditionnels;
    }

    public void setChampsAdditionnels(List<ChampAdditionnelEntity> champsAdditionnels) {
        this.champsAdditionnels = champsAdditionnels;
    }
}
