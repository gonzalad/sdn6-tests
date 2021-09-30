package com.example.sdn6.entity.noeud;

import java.util.UUID;
import org.springframework.data.neo4j.core.schema.GeneratedValue;
import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.Node;

/**
 * TODO: @Node commenté pour éviter MappingException: The schema already contains a node description under the primary label
 *
 * Voir Sdn6Test#whenPersistViewThenVerifyRelationsInGraphArePreserved
 */
//@Node("ObjetMaquette")
public class NoeudRef {

    @GeneratedValue
    @Id
    private Long id;

    private UUID idDefinition;

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
}
