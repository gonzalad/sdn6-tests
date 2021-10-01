package com.example.sdn6.projection;

import java.util.UUID;

public class NoeudProjection {

    private Long id;
    private UUID idDefinition;
    private String code;
    private String firstName;
    private String lastName;
    private NoeudRef child;

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

    public void setFirstname(String libelleCourt) {
        this.firstName = libelleCourt;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public NoeudRef getChild() {
        return child;
    }

    public void setChild(NoeudRef child) {
        this.child = child;
    }
}
