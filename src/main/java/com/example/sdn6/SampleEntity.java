package com.example.sdn6;

import java.util.HashMap;
import java.util.Map;
import org.springframework.data.neo4j.core.schema.CompositeProperty;
import org.springframework.data.neo4j.core.schema.GeneratedValue;
import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.Node;

@Node("Sample")
public class SampleEntity {


    @GeneratedValue
    @Id
    private Long id;

    private String name;

    @CompositeProperty(prefix = "somePrefix")
    private Map<String, Object> composite = new HashMap<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Map<String, Object> getComposite() {
        return composite;
    }

    public void setComposite(Map<String, Object> composite) {
        this.composite = composite;
    }

    @Override
    public String toString() {
        return "SampleEntity{" +
            "id=" + id +
            ", name='" + name + '\'' +
            ", composite=" + composite +
            '}';
    }
}
