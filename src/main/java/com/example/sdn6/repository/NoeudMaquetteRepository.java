package com.example.sdn6.repository;

import java.util.Optional;
import java.util.UUID;
import com.example.sdn6.entity.NoeudMaquetteEntity;
import com.example.sdn6.projection.NoeudProjection;
import org.springframework.data.neo4j.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NoeudMaquetteRepository extends CrudRepository<NoeudMaquetteEntity, Long>, CustomNoeudMaquetteRepository {

    Optional<NoeudMaquetteEntity> findByIdDefinition(UUID idDefinition);

    //@formatter:off
    @Query("MATCH /*+ OGM READ_ONLY */ (om:ObjetMaquette)\n"
        + " WHERE om.idDefinition = $idDefinition\n"
        + " WITH om\n"
        + " OPTIONAL MATCH (om)-[re:A_POUR_ENFANT]->(e)\n"
        + " RETURN om, collect(re) as enfants, collect(e) as ne\n"
    )
    //@formatter:on
    Optional<NoeudProjection> findProjectionByIdDefinition(UUID idDefinition);
}
