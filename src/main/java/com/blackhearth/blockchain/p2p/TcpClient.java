package com.blackhearth.blockchain.p2p;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.Socket;

public class TcpClient implements Client {
    @Override
    public void start() {
        String temp;
        String displayBytes;
        try
        {
            //create input stream
            BufferedReader inFromUser = new BufferedReader(new InputStreamReader(System.in));
            //create client socket, connect to server
            Socket clientSocket = new Socket("localhost",5555);
            //create output stream attached to socket
            DataOutputStream outToServer =
                    new DataOutputStream(clientSocket.getOutputStream());



            System.out.print("Command : ");

            //create input stream attached to socket
            DataInputStream inFromServer = new DataInputStream(clientSocket.getInputStream());

            temp = inFromUser.readLine();

            //send line to server
            outToServer.writeBytes(temp);
            outToServer.writeUTF(temp);
            outToServer.flush();


            //read line from server
            displayBytes = inFromServer.readLine();

            while((displayBytes = inFromServer.readUTF()) != null)
            {
                System.out.print(displayBytes);
            }
            clientSocket.close();
        }
        catch(Exception ex)
        {

        }
    }

    @Override
    public void stop() {

    }
}
