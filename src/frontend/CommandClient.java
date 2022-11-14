package frontend;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class CommandClient {
    int serverPort;
    String serverAddress;

    public CommandClient(int serverPort, String serverAddress) {
        this.serverPort = serverPort;
        this.serverAddress = serverAddress;
    }

    public static void main(String[] args) {
        Socket clientSocket;
        BufferedReader in;
        PrintWriter out;
        Scanner kb = new Scanner(System.in);
        try {
            clientSocket = new Socket(args[0], Integer.parseInt(args[1]));
            out = new PrintWriter(clientSocket.getOutputStream());
            in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            Thread WriteThread = new Thread(new Runnable() {
                String msg;
                @Override
                public void run() {
                    while(clientSocket.isConnected()){
                        msg = kb.nextLine();
                        out.println(msg);
                        out.flush();
                    }
                }
            });
            WriteThread.start();

            Thread ReadThread = new Thread(new Runnable() {
                String msg;
                @Override
                public void run() {
                    try{
                        msg = in.readLine();
                        while(msg != null){
                            System.out.println("Client: " + msg);
                            msg = in.readLine();
                        }
                        System.out.println("Server disconnected");
                        out.close();
                        clientSocket.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            });
            ReadThread.start();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void sendMessage(String message){

    }


}
