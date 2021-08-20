package com.creditas.challenge.util;

import com.creditas.challenge.model.Cav.OpenHours;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;
import java.util.HashMap;
import java.util.Map;

@Converter
public class OpenHoursConverter implements AttributeConverter<Map<String, OpenHours>, String> {
    Logger logger = LoggerFactory.getLogger(OpenHoursConverter.class);

    @Override
    public String convertToDatabaseColumn(Map<String, OpenHours> map) {
        ObjectMapper objectMapper = new ObjectMapper();
        String json = null;
        try {
            json = objectMapper.writeValueAsString(map);
        } catch (JsonProcessingException e) {
            logger.error("{}", e);
        }

        return json;
    }

    @Override
    public Map<String, OpenHours> convertToEntityAttribute(String json) {
        ObjectMapper objectMapper = new ObjectMapper();
        Map<String, OpenHours> map = null;
        try {
            TypeReference<HashMap<String, OpenHours>> typeRef = new TypeReference<HashMap<String, OpenHours>>() {};
            map = objectMapper.readValue(json, typeRef);
        } catch (JsonProcessingException e) {
            logger.error("{}", e);
        }

        return map;
    }
}
