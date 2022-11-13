package backend;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;

public class Server {
    Set<String> userNames = new HashSet<>();
    Set<UserThread> userThreads = new HashSet<>();
    ServerSocket serverSocket;
    Socket clientSocket;
    BufferedReader in;
    PrintWriter out;
    int port;

    public Server(int port) {
        this.port = port;
    }

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        if (args.length < 1) {
            System.out.println("Syntax: java ChatServer <port-number>");
            System.exit(0);
        }

        int port = Integer.parseInt(args[0]);

        Server server = new Server(port);
        server.execute();
    }

    public void execute() {
        try{
            serverSocket = new ServerSocket(7989);
            clientSocket = serverSocket.accept();
            UserThread newUser = new UserThread(clientSocket, this);
            userThreads.add(newUser);
            newUser.start();

            out = new PrintWriter(clientSocket.getOutputStream());
            in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            Thread sender = new Thread(new Runnable() {
                String msg;
                @Override
                public void run() {
                    while(true){
                        msg = sc.nextLine();
                        out.println(msg);
                        out.flush();

                    }
                }
            });
            sender.start();

            Thread receiver = new Thread(new Runnable() {
                String msg;
                @Override
                public void run() {
                    try{
                        msg = in.readLine();
                        while(msg != null){
                            System.out.println("Client: " + msg);
                            msg = in.readLine();
                        }
                        System.out.println("Client disconnected");
                        out.close();
                        clientSocket.close();
                        serverSocket.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            });
            receiver.start();
        }catch (IOException e){
            e.printStackTrace();
        }
    }

    void broadcast(String message, UserThread excludeUser) {
        for (UserThread aUser : userThreads) {
            if (aUser != excludeUser) {
                aUser.sendMessage(message);
            }
        }
    }


}

class UserThread extends Thread{
    private Socket socket;
    private Server server;
    private PrintWriter out;

    public UserThread(Socket socket, Server server) {
        this.socket = socket;
        this.server = server;
    }

    void run(){

    }

    void sendMessage(String message) {
        writer.println(message);
    }
}
