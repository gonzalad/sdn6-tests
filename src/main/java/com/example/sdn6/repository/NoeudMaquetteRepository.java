package com.example.sdn6.repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import com.example.sdn6.entity.NoeudEntity;
import com.example.sdn6.projection.NoeudProjection;
import org.springframework.data.neo4j.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NoeudMaquetteRepository extends CrudRepository<NoeudEntity, Long>, CustomNoeudMaquetteRepository {

    /**
     * A noter cette méthode retourne une liste alors qu'on s'attend à un seul résultat.
     * SDN retourne une ligne par instance de NoeudMaquetteEntity détecté dans le résultat
     */
    //@formatter:off
    @Query("MATCH /*+ OGM READ_ONLY */ e = (racine:ObjetMaquette)-[re:A_POUR_ENFANT*0..]->(om:ObjetMaquette)\n"
        + " WHERE racine.idDefinition = $idDefinition\n"
        + " WITH DISTINCT om\n"
        + " OPTIONAL MATCH (om)-[re:A_POUR_ENFANT*0..]->(e:ObjetMaquette)\n"
        + " RETURN om, collect(re) as enfants, collect(e) as ne\n"
    )
    //@formatter:on
    List<NoeudEntity> findArbre(UUID idDefinition);

    //@formatter:off
    @Query("MATCH /*+ OGM READ_ONLY */ (om:ObjetMaquette)\n"
        + " WHERE om.idDefinition = $idDefinition\n"
        + " RETURN om\n"
    )
    //@formatter:on
    Optional<NoeudEntity> lireNoeud(UUID idDefinition);

    //@formatter:off
//    @Query("MATCH /*+ OGM READ_ONLY */ (om:ObjetMaquette)\n"
//        + " WHERE om.idDefinition = $idDefinition\n"
//        + " WITH om\n"
//        + " OPTIONAL MATCH (om)-[re:A_POUR_ENFANT]->(e)\n"
//        + " RETURN om, collect(re) as enfants, collect(e) as ne\n"
//    )
    //@formatter:on
    Optional<NoeudProjection> findProjectionByIdDefinition(UUID idDefinition);

}
