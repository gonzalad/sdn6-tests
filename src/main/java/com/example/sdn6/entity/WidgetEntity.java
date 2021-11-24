package com.example.sdn6.entity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.data.neo4j.core.schema.CompositeProperty;
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

    @CompositeProperty
    private Map<String, Object> additionalFields = new HashMap<>();

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

    public Map<String, Object> getAdditionalFields() {
        return additionalFields;
    }

    public void setAdditionalFields(Map<String, Object> additionalFields) {
        this.additionalFields = additionalFields;
    }
}
