package frontend;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class Client {
    int serverPort;
    String serverAddress;

    boolean isConnected;

    public boolean isConnected() {
        return isConnected;
    }

    public Client(int serverPort, String serverAddress) {
        this.serverPort = serverPort;
        this.serverAddress = serverAddress;
    }

    public void execute() throws IOException {
        Socket clientSocket;
        BufferedReader in;
        PrintWriter out;
        Scanner kb = new Scanner(System.in);
        try {
            clientSocket = new Socket(serverAddress,serverPort);
            if(clientSocket.isConnected()){
                isConnected = true;
            }else{
                isConnected = false;
            }
        } catch (IOException e) {
            throw new IOException(e);
        }
    }

    public void sendMessage(String message){

    }


}
