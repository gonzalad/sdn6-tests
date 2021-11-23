package com.example.sdn6.entity;

import java.util.List;

public interface WidgetAndChilds {

    String getCode();

    String getLabel();

    List<ChildRef> getChilds();

    TypeRef getType();

    interface ChildRef {

        WidgetRef getTarget();

        boolean isRequired();
    }

    interface TypeRef {
    }

    interface WidgetRef {
    }
}
