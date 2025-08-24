package com.callv2.drive.infrastructure.converter;

import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.callv2.drive.infrastructure.configuration.mapper.Mapper;

@Component
public class JacksonCaster implements Caster {

    private static final ObjectMapper mapper = Mapper.mapper();

    @Override
    public <T> T cast(Object value, Class<T> targetType) {
        return mapper.convertValue(value, targetType);
    }

}
