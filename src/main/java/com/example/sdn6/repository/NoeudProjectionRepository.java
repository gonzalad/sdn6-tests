package com.example.sdn6.repository;

import java.util.Optional;
import java.util.UUID;
import com.example.sdn6.entity.noeud.NoeudViewEntity;
import org.springframework.data.neo4j.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NoeudProjectionRepository extends CrudRepository<NoeudViewEntity, Long> {

    //@formatter:off
    @Query("MATCH /*+ OGM READ_ONLY */ (om:ObjetMaquette)\n"
        + " WHERE om.idDefinition = $idDefinition\n"
        + " WITH om\n"
        + " OPTIONAL MATCH (om)-[re:A_POUR_ENFANT*0..]->(e:ObjetMaquette)\n"
        + " WITH om, collect(re) as enfants, collect(e) as ne\n"
        + " MATCH t = (om)-[:EST_DE_TYPE]->()\n"
        + " RETURN om, enfants, ne, collect(relationships(t)) as type, collect(nodes(t)) as nt\n"
//        + " RETURN om,\n"
//        + "   enfants, e"
        // +","
        //      + "\n"
        //    + "   [[t = (om)-[:A_POUR_ENFANT]->() | nodes(t)]],\n"
        //+ "   [[t = (om)-[:A_POUR_ENFANT]->() | relationships(t)]] as enfants,\n"
        //+ "   [[t = (om)-[:EST_DE_TYPE]->()-[:A_POUR_CHAMP_ADDITIONNEL*0..1]->() | [nodes(t), relationships(t)]]]\n"
/*        + "   [[n = (enfant)-[:EST_DE_NATURE]->() | [nodes(n), relationships(n)]]],"
        + "   [[gh = (enfant)-[:A_POUR_GROUPE_HABILITATION*0..1]->() | [nodes(gh), relationships(gh)]]]"*/
    )
    //@formatter:on
    Optional<NoeudViewEntity> lireNoeudProjection(UUID idDefinition);
}
