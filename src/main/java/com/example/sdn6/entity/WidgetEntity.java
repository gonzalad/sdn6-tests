package com.example.sdn6.entity;

import java.util.ArrayList;
import java.util.List;
import org.springframework.data.neo4j.core.schema.GeneratedValue;
import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.Node;
import org.springframework.data.neo4j.core.schema.Relationship;

@Node("Widget")
public class WidgetEntity {

    @GeneratedValue
    @Id
    private Long id;

    private String code;
    private String label;

    @Relationship(type = "HAS_TYPE")
    private WidgetTypeEntity type;

    @Relationship(type = "HAS_CHILD")
    private List<Child> childs = new ArrayList<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public WidgetTypeEntity getType() {
        return type;
    }

    public void setType(WidgetTypeEntity type) {
        this.type = type;
    }

    public WidgetEntity child(WidgetEntity entity) {
        addChild(entity);
        return this;
    }

    public List<Child> getChilds() {
        return childs;
    }

    public void addChild(WidgetEntity entity) {
        getChilds().add(new Child(entity));
    }

    public void setChilds(List<Child> childs) {
        this.childs = childs;
    }
}
