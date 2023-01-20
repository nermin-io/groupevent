package me.nerminsehic.groupevent.util;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.Map;

public class Converter {
    private Converter() {
    } // non-instantiatable

    private static final ObjectMapper MAPPER = new ObjectMapper();

    public static Map<String, Object> objectToMap(Object obj) {
        return MAPPER.convertValue(obj, new TypeReference<>() {});
    }
}
