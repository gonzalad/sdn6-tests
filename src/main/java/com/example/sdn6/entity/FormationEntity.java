package com.example.sdn6.entity;

import org.springframework.data.neo4j.core.schema.Node;

@Node("Formation")
public class FormationEntity extends NoeudMaquetteEntity {

    @Override
    protected void addLienParent(NoeudMaquetteAPourEnfantRelationEntity lien) {
        throw new UnsupportedOperationException();
    }

    @Override
    protected void removeLienParent(NoeudMaquetteAPourEnfantRelationEntity lien) {
        throw new UnsupportedOperationException();
    }
}
