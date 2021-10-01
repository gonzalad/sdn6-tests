package com.example.sdn6.entity;

import java.util.Objects;
import java.util.UUID;
import org.springframework.data.annotation.ReadOnlyProperty;
import org.springframework.data.neo4j.core.schema.GeneratedValue;
import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.Node;
import org.springframework.data.neo4j.core.schema.Relationship;

@Node("ObjetMaquette")
public class NoeudEntity {

    @GeneratedValue
    @Id
    private Long id;

    private UUID idDefinition;
    private String code;
    private String firstName;
    private String lastName;

    @Relationship(type = "A_POUR_ENFANT")
    private NoeudEntity child;

    @Relationship(type = "A_POUR_ENFANT", direction = Relationship.Direction.INCOMING)
    @ReadOnlyProperty
    private NoeudEntity parent;

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

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public NoeudEntity getChild() {
        return child;
    }

    public void setChild(NoeudEntity child) {
        this.child = child;
        this.child.parent = this;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastname(String lastName) {
        this.lastName = lastName;
    }

    public NoeudEntity getParent() {
        return parent;
    }

    public void setParent(NoeudEntity parent) {
        this.parent = parent;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof NoeudEntity)) {
            return false;
        }
        NoeudEntity that = (NoeudEntity) o;
        return Objects.equals(idDefinition, that.idDefinition);
    }

    @Override
    public int hashCode() {
        return Objects.hash(idDefinition);
    }
}
