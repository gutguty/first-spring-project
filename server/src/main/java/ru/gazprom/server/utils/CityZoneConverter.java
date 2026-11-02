package ru.gazprom.server.utils;


import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import ru.gazprom.server.enums.CityZone;

@Converter
public class CityZoneConverter implements AttributeConverter<CityZone, Integer> {

    @Override
    public Integer convertToDatabaseColumn(CityZone attribute) {
        if (attribute == null) {
            return null;
        }

        return attribute.getValue();
    }

    @Override
    public CityZone convertToEntityAttribute(Integer dbData) {
        if (dbData == null) {
            return null;
        }

        return CityZone.fromValue(dbData);
    }
}
