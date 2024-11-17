package com.zhang.protocol;

public enum MsgStatus {
    SUCCESS(0),
    FAILED(1);

    MsgStatus(int value) {
        this.value = value;
    }

    private int value;

    public int getValue() {
        return value;
    }
}
