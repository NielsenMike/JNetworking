package com.company.client;

import com.company.shared.protocol.Protocol;

import java.util.Scanner;

public class Main {



    public static void main(String[] args) {
        Client client = new Client("127.0.0.1", 5000);
        Scanner scanner = new Scanner(System.in);

        Thread clientThread = new Thread(client);
        clientThread.start();

        String message = "";
        while (!message.equals("Over")){
            message = scanner.nextLine();
            if(message.startsWith("D")){
                client.send(Protocol.Type.MessageEncrypted, message.getBytes());
            }
            else{
                client.send(Protocol.Type.MessagePlainText, message.getBytes());
            }
        }
    }
}
