package com.example.sdn6.entity.noeud;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import org.springframework.data.neo4j.core.schema.GeneratedValue;
import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.Node;
import org.springframework.data.neo4j.core.schema.Relationship;

/**
 * Test pour vérifier si on peut avoir une seconde série d'entités
 *
 * <ul>
 * <li> la première serait pour les opérations orientées graph</li>
 * <li> la seconde (cette classe) pour les opérations sur un noeud (i.e. modifier un descripteur sans avoir besoin
 * de charger tout le graphe)</li>
 * </ul>
 *
 * TODO: @Node commenté pour éviter MappingException: The schema already contains a node description under the primary label
 *
 * Voir Sdn6Test#whenPersistViewThenVerifyRelationsInGraphArePreserved
 */
// @Node("ObjetMaquette")
public abstract class NoeudViewEntity {

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
    private boolean codeModifiable;
    private String contrainteVersion;

    @Relationship(type = "EST_DE_TYPE")
    // @ReadOnlyProperty
    private NoeudTypeRef type;

    @Relationship(type = "A_POUR_ENFANT")
    private List<Enfant> enfants;

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

    public List<Enfant> getEnfants() {
        return enfants;
    }

    public void setEnfants(List<Enfant> enfants) {
        this.enfants = enfants;
    }

    public Enfant addEnfant(NoeudRef enfant, boolean obligatoire) {
        var lien = new Enfant(enfant, obligatoire);
        addEnfant(lien);
        return lien;
    }

    public void addEnfant(Enfant lien) {
        if (getEnfants() == null) {
            setEnfants(new ArrayList<>());
        }
        getEnfants().add(lien);
    }

    public NoeudTypeRef getType() {
        return type;
    }

    public void setType(NoeudTypeRef type) {
        this.type = type;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof NoeudViewEntity)) {
            return false;
        }
        NoeudViewEntity that = (NoeudViewEntity) o;
        return Objects.equals(idDefinition, that.idDefinition);
    }

    @Override
    public int hashCode() {
        return Objects.hash(idDefinition);
    }
}
