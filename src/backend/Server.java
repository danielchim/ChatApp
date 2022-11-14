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
        try {
            ServerSocket serverSocket = new ServerSocket(port);
            while (true) {
                clientSocket = serverSocket.accept();
                System.out.println("New user connected");

                UserThread newUser = new UserThread(clientSocket, this);
                userThreads.add(newUser);
                newUser.start();

            }
        }catch (IOException e){
            e.printStackTrace();
        }
    }

    public void removeUser(UserThread user){
        userThreads.remove(user);
    }

    void broadcast(String message, UserThread excludeUser) {
        System.out.println(userThreads.size());
        for (UserThread aUser : userThreads) {
            if (aUser != excludeUser) {
                aUser.sendMessage(message);
            }
        }
    }


}

class UserThread extends Thread{
    private Socket clientSocket;
    private Server server;
    private PrintWriter out;
    BufferedReader in;
    String msg;

    public UserThread(Socket clientSocket, Server server) {
        this.clientSocket = clientSocket;
        this.server = server;
    }

    public void run(){
        try{
            out = new PrintWriter(clientSocket.getOutputStream(),true);
            in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            msg = in.readLine();
            while(msg != null){
                server.broadcast(msg,this);
                msg = in.readLine();
            }
            System.out.println("Client disconnected");
            server.removeUser(this);
            out.close();
            clientSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    void sendMessage(String message) {
        out.println(message);
    }
}
