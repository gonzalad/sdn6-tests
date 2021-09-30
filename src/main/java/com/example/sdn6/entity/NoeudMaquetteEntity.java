package com.example.sdn6.entity;

import java.util.UUID;
import org.springframework.data.neo4j.core.schema.GeneratedValue;
import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.Node;

@Node("ObjetMaquette")
public class NoeudMaquetteEntity {

    @GeneratedValue
    @Id
    private Long id;
    private UUID idDefinition;
    private String code;
    private boolean readOnly;

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

    public boolean isReadOnly() {
        return readOnly;
    }

    public void setReadOnly(boolean readOnly) {
        this.readOnly = readOnly;
    }
}
