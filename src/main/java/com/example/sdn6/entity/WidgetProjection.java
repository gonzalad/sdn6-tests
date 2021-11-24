package com.example.sdn6.entity;

import java.util.Map;

public interface WidgetProjection {

    String getCode();

    String getLabel();

    Map<String, Object> getAdditionalFields();
}
