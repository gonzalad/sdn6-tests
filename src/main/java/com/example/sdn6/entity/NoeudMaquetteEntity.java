package com.example.sdn6.entity;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import org.springframework.data.annotation.ReadOnlyProperty;
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
    private String libelleCourt;
    private String libelleLong;

    @Relationship(type = "A_POUR_ENFANT")
    private NoeudMaquetteAPourEnfantRelationEntity enfants;

    @Relationship(type = "A_POUR_ENFANT", direction = Relationship.Direction.INCOMING)
    @ReadOnlyProperty
    private List<NoeudMaquetteAPourEnfantRelationEntity> parents;

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

    public String getLibelleCourt() {
        return libelleCourt;
    }

    public void setLibelleCourt(String libelleCourt) {
        this.libelleCourt = libelleCourt;
    }

    public NoeudMaquetteAPourEnfantRelationEntity getEnfants() {
        return enfants;
    }

    public void setEnfants(NoeudMaquetteAPourEnfantRelationEntity enfants) {
        this.enfants = enfants;
    }

    public String getLibelleLong() {
        return libelleLong;
    }

    public void setLibelleLong(String libelleLong) {
        this.libelleLong = libelleLong;
    }

    public NoeudMaquetteAPourEnfantRelationEntity addEnfant(NoeudMaquetteEntity enfant, boolean obligatoire) {
        var lien = new NoeudMaquetteAPourEnfantRelationEntity(enfant, obligatoire);
        addEnfant(lien);
        return lien;
    }

    public void addEnfant(NoeudMaquetteAPourEnfantRelationEntity lien) {
        setEnfants(lien);
    }

    private void addLienParent(NoeudMaquetteAPourEnfantRelationEntity lien) {
        if (getParents() == null) {
            setParents(new ArrayList<>());
        }
        getParents().add(lien);
    }

    private void removeLienParent(NoeudMaquetteAPourEnfantRelationEntity lien) {
        if (getParents() == null) {
            setParents(new ArrayList<>());
        }
        getParents().remove(lien);
    }

    public List<NoeudMaquetteAPourEnfantRelationEntity> getParents() {
        return parents;
    }

    public void setParents(List<NoeudMaquetteAPourEnfantRelationEntity> parents) {
        this.parents = parents;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof NoeudMaquetteEntity)) {
            return false;
        }
        NoeudMaquetteEntity that = (NoeudMaquetteEntity) o;
        return Objects.equals(idDefinition, that.idDefinition);
    }

    @Override
    public int hashCode() {
        return Objects.hash(idDefinition);
    }
}
