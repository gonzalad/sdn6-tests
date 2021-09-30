package com.example.sdn6.entity.noeud;

import org.springframework.data.neo4j.core.schema.GeneratedValue;
import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.Node;

/**
 * TODO: @Node commenté pour éviter MappingException: The schema already contains a node description under the primary label
 *
 * Voir Sdn6Test#whenPersistViewThenVerifyRelationsInGraphArePreserved
 */
// @Node("NoeudType")
public class NoeudTypeRef {

    @GeneratedValue
    @Id
    private Long id;
    private String code;
    private String codeStructure;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getCodeStructure() {
        return codeStructure;
    }

    public void setCodeStructure(String codeStructure) {
        this.codeStructure = codeStructure;
    }
}
