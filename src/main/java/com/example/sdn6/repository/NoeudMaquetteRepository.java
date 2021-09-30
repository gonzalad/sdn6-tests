package com.example.sdn6.repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import com.example.sdn6.entity.NoeudMaquetteEntity;
import com.example.sdn6.projection.NoeudProjection;
import org.springframework.data.neo4j.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NoeudMaquetteRepository extends CrudRepository<NoeudMaquetteEntity, Long>, CustomNoeudMaquetteRepository {

    //@formatter:off
    @Query("MATCH /*+ OGM READ_ONLY */ e = (racine:ObjetMaquette)-[re:A_POUR_ENFANT*0..]->(om:ObjetMaquette)\n"
        + " WHERE racine.idDefinition = $idDefinition\n"
        + " WITH DISTINCT om\n"
        + " OPTIONAL MATCH (om)-[re:A_POUR_ENFANT*0..]->(e:ObjetMaquette)\n"
        + " WITH om, collect(re) as enfants, collect(e) as ne\n"
        + " MATCH t = (om)-[:EST_DE_TYPE]->()-[:A_POUR_CHAMP_ADDITIONNEL*0..1]->()\n"
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
    List<NoeudMaquetteEntity> findArbre(UUID idDefinition);

    //@formatter:off
    @Query("MATCH /*+ OGM READ_ONLY */ (om:ObjetMaquette)\n"
        + " WHERE om.idDefinition = $idDefinition\n"
        + " WITH om\n"
        + " MATCH t = (om)-[:EST_DE_TYPE]->()-[:A_POUR_CHAMP_ADDITIONNEL*0..1]->()\n"
        + " RETURN om, collect(relationships(t)) as type, collect(nodes(t)) as nt\n"
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
    Optional<NoeudMaquetteEntity> lireNoeud(UUID idDefinition);

    //@formatter:off
    @Query("MATCH /*+ OGM READ_ONLY */ (om:ObjetMaquette)\n"
        + " WHERE om.idDefinition = $idDefinition\n"
        + " WITH om\n"
        + " MATCH t = (om)-[:EST_DE_TYPE]->()-[:A_POUR_CHAMP_ADDITIONNEL*0..1]->()\n"
        + " WITH om, collect(relationships(t)) as type, collect(nodes(t)) as nt\n"
        + " OPTIONAL MATCH (om)-[re:A_POUR_ENFANT]->(e)\n"
        + " RETURN om, type, nt, collect(re) as enfants, collect(e) as ne\n"
    )
    //@formatter:on
    Optional<NoeudProjection> findProjectionByIdDefinition(UUID idDefinition);

}
