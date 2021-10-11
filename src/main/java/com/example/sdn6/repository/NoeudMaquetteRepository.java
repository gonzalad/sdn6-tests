package com.example.sdn6.repository;

import java.util.List;
import com.example.sdn6.entity.NoeudMaquetteEntity;
import com.example.sdn6.projection.NoeudAndFormationsParentesInterface;
import com.example.sdn6.projection.NoeudAndFormationsParentesResult;
import org.springframework.data.neo4j.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NoeudMaquetteRepository extends CrudRepository<NoeudMaquetteEntity, Long>, CustomNoeudMaquetteRepository {

    @Query(
        " MATCH pof = (om:ObjetMaquette)-[r:EST_DE_TYPE]->(type:NoeudType)\n"
            + " WHERE \n"
            + " om.code = $code\n"
            + "WITH om, collect(type) as types, collect(r) as rs\n"
            + "OPTIONAL MATCH p = (om)<-[:A_POUR_ENFANT*1..]-(f:Formation)\n"
            + "RETURN om,\n"
            + "    collect(f) as formationsParentes,\n"
            + "    types, rs"
    )
    List<NoeudAndFormationsParentesResult> findAllNoeudAndFormationsParentesByCodeWithQueryMethod(String code);

    @Query(
        " MATCH pof = (om:ObjetMaquette)-[r:EST_DE_TYPE]->(type:NoeudType)\n"
            + " WHERE \n"
            + " om.code = $code\n"
            + "WITH om, collect(type) as types, collect(r) as rs\n"
            + "OPTIONAL MATCH p = (om)<-[:A_POUR_ENFANT*1..]-(f:Formation)\n"
            + "RETURN om,\n"
            + "    collect(f) as formationsParentes,\n"
            + "    types, rs"
    )
    List<NoeudAndFormationsParentesInterface> findAllNoeudAndFormationsParentesByCodeWithQueryMethodAndProjectedInterface(String code);
}
