package com.company.client;

import com.company.shared.Host;
import com.company.shared.RSAKeyGenerator;
import com.company.shared.protocol.Protocol;

import javax.crypto.Cipher;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.security.*;

public final class Client extends Host {

    private PublicKey serverPublicKey = null;

    public Client(String address, int port){
        try {
            this.socket = new Socket(address, port);
            this.in = new DataInputStream(this.socket.getInputStream());
            this.out = new DataOutputStream(this.socket.getOutputStream());
        }
        catch (IOException e){
            e.printStackTrace();
            this.close();
        }
    }

    @Override
    protected void receive(){
         try{
             if(this.in.available() > 0){
                 Protocol protocol = Protocol.readByteArray(this.in);
                 switch (protocol.getType()){
                     case HelloClient:
                         String serverName = new String(protocol.getPayload());
                         System.out.println("Client - HelloClient: " + serverName);
                         break;
                     case MessagePlainText:
                         String serverMessage = new String(protocol.getPayload());
                         System.out.println("Client - MessagePlainText: " + serverMessage);
                         break;
                     case AsyncKeyExchangeServer:
                         this.serverPublicKey = RSAKeyGenerator.validatePublicKey(protocol.getPayload());
                         System.out.println("Client - AsyncKeyExchangeServer");
                         break;
                 }
             }
         }
         catch (IOException | GeneralSecurityException e){
             this.close();
         }
    }

    @Override
    public void send(Protocol.Type type, byte[] data){
        try {
            if(type.equals(Protocol.Type.MessageEncrypted)){
                data = this.encrypt(data);
            }
            Protocol.writeByteArray(this.out, type, data);
            this.out.flush();
        }
        catch (IOException e){
            e.printStackTrace();
        }
    }

    @Override
    public void run(){
        while (!this.socket.isClosed()){
            this.receive();
        }
    }

    public byte[] encrypt(byte[] bytes){
        try {
            Cipher cipher = Cipher.getInstance("RSA");
            cipher.init(Cipher.ENCRYPT_MODE, this.serverPublicKey);
            bytes = cipher.doFinal(bytes);
        }
        catch (GeneralSecurityException e){
            e.printStackTrace();
            this.close();
        }
        return bytes;
    }
}

