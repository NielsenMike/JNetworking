package com.company.shared;

import com.company.shared.protocol.Protocol;

import java.io.Closeable;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public abstract class Host implements Runnable, Closeable {

    protected Socket socket = null;
    protected DataInputStream in = null;
    protected DataOutputStream out = null;

    public abstract void run();
    protected abstract void receive();
    public abstract void send(Protocol.Type type, byte[] data);
    public void close(){
        try {
            socket.close();
            in.close();
            out.close();
        }
        catch (IOException e){
            e.printStackTrace();
        }
    }

}
