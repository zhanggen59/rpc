package com.zhang.serialization;

import lombok.Getter;

public enum SerializationType {
    HESSIAN(0x10),
    JSON(0x20);

    @Getter
    private int value;

    SerializationType(int value) {
        this.value = value;
    }

    public static SerializationType findByType(byte type) {
        for (SerializationType value : SerializationType.values()) {
            if (value.getValue() == type) {
                return value;
            }
        }
        return HESSIAN;
    }
}
