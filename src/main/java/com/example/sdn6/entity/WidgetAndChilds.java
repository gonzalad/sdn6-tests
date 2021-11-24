package com.example.sdn6.entity;

import java.util.List;
import java.util.Map;

public interface WidgetAndChilds {

    String getCode();

    String getLabel();

    List<ChildRef> getChilds();

    TypeRef getType();

    Map<String, Object> getAdditionalFields();

    interface ChildRef {

        WidgetRef getTarget();

        boolean isRequired();
    }

    interface TypeRef {
    }

    interface WidgetRef {
    }
}
