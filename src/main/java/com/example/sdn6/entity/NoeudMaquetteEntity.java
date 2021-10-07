package com.example.sdn6.entity;

import java.util.List;
import java.util.UUID;
import org.springframework.data.neo4j.core.schema.GeneratedValue;
import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.Node;
import org.springframework.data.neo4j.core.schema.Relationship;

@Node("ObjetMaquette")
public class NoeudMaquetteEntity {

    @GeneratedValue
    @Id
    private Long id;
    private UUID idDefinition;
    private String code;

    @Relationship(type = "EST_DE_TYPE")
    private NoeudTypeEntity type;

    @Relationship(type = "A_POUR_ENFANT")
    private List<NoeudMaquetteEntity> enfants;

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

    public NoeudTypeEntity getType() {
        return type;
    }

    public void setType(NoeudTypeEntity type) {
        this.type = type;
    }

    public List<NoeudMaquetteEntity> getEnfants() {
        return enfants;
    }

    public void setEnfants(List<NoeudMaquetteEntity> enfants) {
        this.enfants = enfants;
    }
}
