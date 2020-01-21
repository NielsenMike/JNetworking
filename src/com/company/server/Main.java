package com.company.server;

import com.company.shared.protocol.Protocol;

import java.util.Scanner;

public class Main {


    public static void main(String[] args) {
        Server server = new Server(5000);
        Scanner scanner = new Scanner(System.in);

        Thread serverThread = new Thread(server);
        serverThread.start();

        String message = "";
        while (!message.equals("Over")){
            message = scanner.nextLine();
            server.send(Protocol.Type.MessagePlainText, message.getBytes());
        }
    }
}
