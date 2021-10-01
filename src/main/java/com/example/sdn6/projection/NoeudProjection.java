package com.example.sdn6.projection;

import java.util.List;
import java.util.UUID;

public class NoeudProjection {

    private Long id;
    private UUID idDefinition;
    private String code;
    private String libelleCourt;
    private String libelleLong;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    private Enfant enfants;

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

    public Enfant getEnfants() {
        return enfants;
    }

    public void setEnfants(Enfant enfants) {
        this.enfants = enfants;
    }
}
