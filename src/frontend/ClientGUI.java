package frontend;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

public class ClientGUI{
    private JTextArea chatLog;
    private JTextField messageInput;
    private JButton sendButton;
    private JPanel mainPane;
    private static boolean isConnected;

    public ClientGUI(Client c) {
        sendButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

            }
        });
    }

    public static void main(String[] args) {
        Client c;
        String[] serverAddress = new String[2];
        while(!isConnected) {
            try{
                String userInput = JOptionPane.showInputDialog("Please enter the server address");
                serverAddress = userInput.split(":");
                c = new Client(Integer.parseInt(serverAddress[1]), serverAddress[0]);
                c.execute();
                if(c.isConnected()){
                    isConnected = true;
                    JFrame frame = new JFrame("ChatGUI");
                    frame.setContentPane(new ClientGUI(c).mainPane);
                    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                    frame.pack();
                    frame.setVisible(true);
                }

            }catch (IOException e){
                JOptionPane.showConfirmDialog(null,"Failed to connect to server");
            }
            catch (ArrayIndexOutOfBoundsException e){
                JOptionPane.showConfirmDialog(null,"Wrong server ip address");
            }
        }
    }

    }

