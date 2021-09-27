package com.example.sdn6.entity;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import org.springframework.data.neo4j.core.schema.GeneratedValue;
import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.Node;
import org.springframework.data.neo4j.core.schema.Relationship;

@Node("ObjetMaquette")
public abstract class NoeudMaquetteEntity {

    @GeneratedValue
    @Id
    private Long id;

    // @Convert(UuidStringConverter.class)
    private UUID idImmuable;

    private String codeStructure;

    // @Convert(UuidStringConverter.class)
    private UUID idDefinition;

    private String code;
    private String libelleCourt;
    private String libelleLong;
    private boolean codeModifiable;
    private String contrainteVersion;

    @Relationship(type = "EST_DE_TYPE")
    private NoeudTypeEntity type;

    @Relationship(type = "A_POUR_ENFANT")
    private List<NoeudMaquetteAPourEnfantRelationEntity> enfants;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

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

    public List<NoeudMaquetteAPourEnfantRelationEntity> getEnfants() {
        return enfants;
    }

    public void setEnfants(List<NoeudMaquetteAPourEnfantRelationEntity> enfants) {
        this.enfants = enfants;
    }

    public NoeudMaquetteAPourEnfantRelationEntity addEnfant(NoeudMaquetteEntity enfant, boolean obligatoire) {
        var lien = new NoeudMaquetteAPourEnfantRelationEntity(enfant, obligatoire);
        addEnfant(lien);
        return lien;
    }

    public void addEnfant(NoeudMaquetteAPourEnfantRelationEntity lien) {
        if (getEnfants() == null) {
            setEnfants(new ArrayList<>());
        }
        getEnfants().add(lien);
        // TODO: bidirection (lien.getEnfant()).addLienParent(lien);
    }

    protected abstract void addLienParent(NoeudMaquetteAPourEnfantRelationEntity lien);

    protected abstract void removeLienParent(NoeudMaquetteAPourEnfantRelationEntity lien);

    public NoeudTypeEntity getType() {
        return type;
    }

    public void setType(NoeudTypeEntity type) {
        this.type = type;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof NoeudMaquetteEntity)) {
            return false;
        }
        NoeudMaquetteEntity that = (NoeudMaquetteEntity)o;
        return Objects.equals(idDefinition, that.idDefinition);
    }

    @Override
    public int hashCode() {
        return Objects.hash(idDefinition);
    }
}
