package com.example.sdn6.entity;

import org.springframework.data.neo4j.core.schema.GeneratedValue;
import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.RelationshipProperties;
import org.springframework.data.neo4j.core.schema.TargetNode;

@RelationshipProperties
public class Child {

    @GeneratedValue
    @Id
    private Long id;

    @TargetNode
    private WidgetEntity target;

    Child() {
    }

    public Child(WidgetEntity target) {
        this.target = target;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public WidgetEntity getTarget() {
        return target;
    }

    public void setTarget(WidgetEntity target) {
        this.target = target;
    }
}
