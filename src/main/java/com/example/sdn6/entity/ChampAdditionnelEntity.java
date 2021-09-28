package com.example.sdn6.entity;

import org.springframework.data.neo4j.core.schema.GeneratedValue;
import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.Node;

@Node("ChampAdditionnel")
public class ChampAdditionnelEntity {

    @Id
    @GeneratedValue
    private Long id;
    private String code;
    private String libelle;
    private boolean valeurDefautBooleen;
    private Double valeurDefautNum;
    private String valeurDefautString;
    private TypeChampAdditionnel type;
    private int ordre;


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

    public String getLibelle() {
        return libelle;
    }

    public void setLibelle(String libelle) {
        this.libelle = libelle;
    }

    public Boolean getValeurDefautBooleen() {
        return valeurDefautBooleen;
    }

    public void setValeurDefautBooleen(boolean valeurDefautBooleen) {
        this.valeurDefautBooleen = valeurDefautBooleen;
    }

    public Double getValeurDefautNum() {
        return valeurDefautNum;
    }

    public void setValeurDefautNum(Double valeurDefautNum) {
        this.valeurDefautNum = valeurDefautNum;
    }

    public String getValeurDefautString() {
        return valeurDefautString;
    }

    public void setValeurDefautString(String valeurDefautString) {
        this.valeurDefautString = valeurDefautString;
    }

    public TypeChampAdditionnel getType() {
        return type;
    }

    public void setType(TypeChampAdditionnel type) {
        this.type = type;
    }

    public int getOrdre() {
        return ordre;
    }

    public void setOrdre(int ordre) {
        this.ordre = ordre;
    }

    /*
    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ChampAdditionnelEntity)) {
            return false;
        }
        ChampAdditionnelEntity that = (ChampAdditionnelEntity) o;
        return code.equals(that.code);
    }

    @Override
    public int hashCode() {
        return code.hashCode();
    }

     */
}
