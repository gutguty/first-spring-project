package ru.gazprom.server.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum CityZone {
    CENTER(1),
    SUBURB(2),
    COUNTRYSIDE(3),
    UNDEFINED(0);


    private final Integer value;

    public static CityZone fromValue(Integer value) {

        for (CityZone zone: values()) {

            if (zone.getValue().equals(value)) {
                return zone;
            }
        }
        return UNDEFINED;
    }
}
