package com.example.sdn6.repository;

import java.util.Optional;
import com.example.sdn6.entity.WidgetEntity;
import com.example.sdn6.entity.WidgetSummary;
import org.springframework.data.neo4j.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WidgetRepository extends CrudRepository<WidgetEntity, Long> {

    Optional<WidgetEntity> findByCode(String code);

    Optional<WidgetSummary> findSummaryByCode(String code);

    //@formatter:off
    @Query(value = "MATCH (w:Widget) WHERE w.code = $code\n"
        + "MATCH (w)-[rt:HAS_TYPE]->(t:WidgetType)\n"
        + "MATCH (w)-[rc:HAS_CHILD]->(c:Widget)\n"
        + "RETURN w, collect(rt), collect(t), collect(rc), collect(c)"
    )
    //@formatter:on
    Optional<WidgetEntity> findWithChildrenByCode(String code);
}
