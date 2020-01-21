package com.company.server;
import com.company.shared.Host;
import com.company.shared.RSAKeyGenerator;
import com.company.shared.protocol.Protocol;

import javax.crypto.Cipher;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.*;
import java.security.GeneralSecurityException;
import java.security.KeyPair;

/**
 * TODO: Implement Logger, AES Secret Encryption, Error Handling (Closing Connection)
 *
 */

public final class Server extends Host {

    private ServerSocket serverSocket = null;
    private KeyPair keyPair = null;

    private int keySize = 2048;
    private String serverName = "Java Server";

    public Server(int port){
        try{
            this.serverSocket = new ServerSocket(port);
            this.keyPair = RSAKeyGenerator.generateKey(this.keySize);
            System.out.println("Server started");

            System.out.println("Waiting for Client!");
            this.socket = serverSocket.accept();
            System.out.println("Client accepted!");

            this.in = new DataInputStream(this.socket.getInputStream());
            this.out = new DataOutputStream(this.socket.getOutputStream());

            this.send(Protocol.Type.HelloClient, this.serverName.getBytes());
            this.send(Protocol.Type.AsyncKeyExchangeServer, this.keyPair.getPublic().getEncoded());
        }
        catch (IOException e){
            e.printStackTrace();
        }
    }

    protected void receive(){
        try{
            if(this.in.available() > 0){
                Protocol protocol = Protocol.readByteArray(this.in);
                switch (protocol.getType()){
                    case MessagePlainText:
                        String message = new String(protocol.getPayload());
                        System.out.println("Server - MessagePlainText " + message);
                        break;
                    case MessageEncrypted:
                        String messageCipher = new String(protocol.getPayload());
                        String messageText = new String(this.decrypt(protocol.getPayload()));
                        System.out.println("Server - MessageEncrypted: " + messageCipher);
                        System.out.println("Server - MessageEncrypted: " + messageText);
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

    private byte[] decrypt(byte[] bytes) throws GeneralSecurityException {
        Cipher cipher = Cipher.getInstance("RSA");
        cipher.init(Cipher.DECRYPT_MODE, this.keyPair.getPrivate());
        return cipher.doFinal(bytes);
    }
}
