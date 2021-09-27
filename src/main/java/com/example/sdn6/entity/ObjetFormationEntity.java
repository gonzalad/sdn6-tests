package com.example.sdn6.entity;

import java.util.ArrayList;
import java.util.List;
import org.springframework.data.neo4j.core.schema.Node;
import org.springframework.data.neo4j.core.schema.Relationship;

@Node("ObjetFormation")
public class ObjetFormationEntity extends NoeudMaquetteEntity {

    @Relationship(type = "A_POUR_ENFANT", direction = Relationship.Direction.INCOMING)
    private List<NoeudMaquetteAPourEnfantRelationEntity> parents;

    public List<NoeudMaquetteAPourEnfantRelationEntity> getParents() {
        return parents;
    }

    public void setParents(List<NoeudMaquetteAPourEnfantRelationEntity> parents) {
        this.parents = parents;
    }

    @Override
    protected void addLienParent(NoeudMaquetteAPourEnfantRelationEntity lien) {
        if (getParents() == null) {
            setParents(new ArrayList<>());
        }
        getParents().add(lien);
    }

    @Override
    protected void removeLienParent(NoeudMaquetteAPourEnfantRelationEntity lien) {
        if (getParents() == null) {
            setParents(new ArrayList<>());
        }
        getParents().remove(lien);
    }
}
