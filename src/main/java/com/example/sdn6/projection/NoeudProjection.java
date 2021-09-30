package com.example.sdn6.projection;

import java.util.List;
import java.util.UUID;

public class NoeudProjection {

    private UUID idImmuable;

    private String codeStructure;

    private UUID idDefinition;

    private String code;
    private String libelleCourt;
    private String libelleLong;
    private boolean codeModifiable;
    private String contrainteVersion;

    //private Type type;

    private List<Enfant> enfants;

    public UUID getIdImmuable() {
        return idImmuable;
    }

    public void setIdImmuable(UUID idImmuable) {
        this.idImmuable = idImmuable;
    }

    public String getCodeStructure() {
        return codeStructure;
    }

    public void setCodeStructure(String codeStructure) {
        this.codeStructure = codeStructure;
    }

    public UUID getIdDefinition() {
        return idDefinition;
    }

    public void setIdDefinition(UUID idDefinition) {
        this.idDefinition = idDefinition;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getLibelleCourt() {
        return libelleCourt;
    }

    public void setLibelleCourt(String libelleCourt) {
        this.libelleCourt = libelleCourt;
    }

    public String getLibelleLong() {
        return libelleLong;
    }

    public void setLibelleLong(String libelleLong) {
        this.libelleLong = libelleLong;
    }

    public boolean isCodeModifiable() {
        return codeModifiable;
    }

    public void setCodeModifiable(boolean codeModifiable) {
        this.codeModifiable = codeModifiable;
    }

    public String getContrainteVersion() {
        return contrainteVersion;
    }

    public void setContrainteVersion(String contrainteVersion) {
        this.contrainteVersion = contrainteVersion;
    }

    /*
    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

     */

    public List<Enfant> getEnfants() {
        return enfants;
    }

    public void setEnfants(List<Enfant> enfants) {
        this.enfants = enfants;
    }
}
