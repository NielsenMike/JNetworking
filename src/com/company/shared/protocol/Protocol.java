package com.company.shared.protocol;

import java.io.*;

public class Protocol {

    public enum Type{
        HelloClient(1),
        AsyncKeyExchangeServer(2),
        MessageEncrypted(3),
        MessagePlainText(4);

        private final int value;

        Type(int value){
            this.value = value;
        }

        public int getValue(){
            return this.value;
        }

        public static Type fromInteger(int x) {
            switch(x) {
                case 1:
                    return HelloClient;
                case 2:
                    return AsyncKeyExchangeServer;
                case 3:
                    return MessageEncrypted;
                case 4:
                    return MessagePlainText;
            }
            return null;
        }
    }

    private Type type;
    private int payloadSize;
    private  byte[] payload;

    private Protocol(Type type, int payloadSize, byte[] payload){
        this.type = type;
        this.payloadSize = payloadSize;
        this.payload = payload;
    }

    public static void writeByteArray(DataOutputStream output, Type type, byte[] payload) throws IOException {
        output.writeInt(type.getValue());
        output.writeInt(payload.length);
        output.write(payload);
    }

    public static Protocol readByteArray(DataInputStream input) throws IOException {
        Type type = Type.fromInteger(input.readInt());
        int payloadSize = input.readInt();
        byte[] payload = input.readNBytes(payloadSize);
        return new Protocol(type, payloadSize, payload);
    }

    public Type getType(){
        return this.type;
    }

    public byte[] getPayload(){
        return this.payload;
    }
}
