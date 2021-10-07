package com.example.sdn6.repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.BiFunction;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;
import com.example.sdn6.entity.FormationEntity;
import com.example.sdn6.entity.NoeudMaquetteEntity;
import com.example.sdn6.projection.NoeudAndFormationsParentesResult;
import org.neo4j.driver.Value;
import org.neo4j.driver.types.MapAccessor;
import org.neo4j.driver.types.TypeSystem;
import org.springframework.data.neo4j.core.Neo4jClient;
import org.springframework.data.neo4j.core.Neo4jTemplate;
import org.springframework.data.neo4j.core.mapping.Neo4jMappingContext;

public class CustomNoeudMaquetteRepositoryImpl implements CustomNoeudMaquetteRepository {

    private final Neo4jTemplate neo4jTemplate;
    private final Neo4jClient neo4jClient;
    private final Neo4jMappingContext neo4jMappingContext;

    public CustomNoeudMaquetteRepositoryImpl(Neo4jTemplate neo4jTemplate, Neo4jClient neo4jClient, Neo4jMappingContext neo4jMappingContext) {
        this.neo4jTemplate = neo4jTemplate;
        this.neo4jClient = neo4jClient;
        this.neo4jMappingContext = neo4jMappingContext;
    }

    @Override
    public List<NoeudAndFormationsParentesResult> findAllNoeudAndFormationsParentesByCode(String code) {
        var query = "";
        query += " MATCH pof = (om:ObjetMaquette)-[r:EST_DE_TYPE]->(type:NoeudType)\n"
            + " WHERE \n"
            + " om.code = $code\n";
        query += "WITH om, collect(type) as types, collect(r) as rs\n";
        query += "RETURN \n";
        query += "    om as om,\n";
        query += "    [(om)<-[:A_POUR_ENFANT*1..]-(f:Formation) | f] as formationsParentes,\n";
        query += "    types, rs";
        //@formatter:on
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("code", code);
        List<NoeudAndFormationsParentesResult> noeudsAndFormations = new ArrayList<>();
        BiFunction<TypeSystem, MapAccessor, NoeudMaquetteEntity> mappingFunction = neo4jMappingContext.getRequiredMappingFunctionFor(NoeudMaquetteEntity.class);
        neo4jClient.query(query) //
            .bindAll(parameters) //
            .fetchAs(NoeudMaquetteEntity.class) //
            .mappedBy((t, r) -> {
                NoeudMaquetteEntity om = mappingFunction.apply(t, r.get("om"));
                List<FormationEntity> formationsParentes = this.asListNoeudMaquetteEntity(r.get("formationsParentes"), mappingFunction, t, FormationEntity.class);
                noeudsAndFormations.add(new NoeudAndFormationsParentesResult(om, formationsParentes));
                return om;
            }) //
            .all();
        return noeudsAndFormations;
    }

    private <S extends NoeudMaquetteEntity> List<S> asListNoeudMaquetteEntity(Value value, //
                                                                              BiFunction<TypeSystem, MapAccessor, NoeudMaquetteEntity> mappingFunction, //
                                                                              TypeSystem t, //
                                                                              Class<S> entityType) {
        return StreamSupport.stream(value.values().spliterator(), false) //
            .map(v -> mappingFunction.apply(t, v)) //
            .map(entityType::cast) //
            .collect(Collectors.toList());
    }
}
