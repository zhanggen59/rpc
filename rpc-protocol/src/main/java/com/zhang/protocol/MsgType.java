package com.zhang.protocol;

public enum MsgType {
    REQUEST(1),
    RESPONSE(2),
    HEARTBEAT(3);

    private int value;

    public int getValue() {
        return value;
    }

    MsgType(int value) {
        this.value = value;
    }

    public static MsgType findByType(int type) {
        for (MsgType msgType : MsgType.values()) {
            if (msgType.getValue() == type) {
                return msgType;
            }
        }

        return null;
    }
}
