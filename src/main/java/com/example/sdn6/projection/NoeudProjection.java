package com.example.sdn6.projection;

import java.util.UUID;

public class NoeudProjection {

    private Long id;
    private UUID idDefinition;
    private String code;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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
}
