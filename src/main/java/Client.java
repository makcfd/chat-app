import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;


public class Client implements Runnable {
    // main window
    static JFrame chat = new JFrame("Chat");
    // chat window
    static JTextArea textExchange = new JTextArea(22, 40);
    static JTextField messageField = new JTextField(38);
    static JLabel separator = new JLabel("            ");
    static JButton sendMessage = new JButton("Send");
    static BufferedReader in;
    static PrintWriter out;
    static JLabel nameLabel = new JLabel("");
    private String ipAddress;
    private int port;
    private String nickName;

    Client(String ipAddress, int port) {

        chat.setLayout(new FlowLayout());
        // add UI components
        chat.add(nameLabel);
        chat.add(new JScrollPane(textExchange));
        chat.add(separator);
        chat.add(messageField);
        chat.add(sendMessage);

        chat.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        chat.setSize(475, 500);
        chat.setVisible(true);
        textExchange.setEnabled(false);
        sendMessage.addActionListener(new Listener());
        messageField.addActionListener(new Listener());
        this.ipAddress = ipAddress;
        this.port = port;
    }

    Client(String ipAddress, int port, String nickName) {

        chat.setLayout(new FlowLayout());
        // add UI components
        chat.add(nameLabel);
        chat.add(new JScrollPane(textExchange));
        chat.add(separator);
        chat.add(messageField);
        chat.add(sendMessage);

        chat.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        chat.setSize(475, 500);
        chat.setVisible(true);
        textExchange.setEnabled(false);
        sendMessage.addActionListener(new Listener());
        messageField.addActionListener(new Listener());
        this.ipAddress = ipAddress;
        this.port = port;
        this.nickName = nickName;
    }

    // main logic of a chat client
    @Override
    public void run() {
        if (ipAddress == null) {
            ipAddress = JOptionPane.showInputDialog(
                    chat,
                    "Enter IP Address",
                    "IP Address requeired",
                    JOptionPane.PLAIN_MESSAGE);
        }

        System.out.println("Client started");
        Socket soc = null;
        try { soc = new Socket(ipAddress, port); } catch (IOException e) { return; }
        try { in = new BufferedReader(new InputStreamReader(soc.getInputStream())); } catch (IOException e) { return; }
        try { out = new PrintWriter(soc.getOutputStream(), true); } catch (IOException e) { return; }

        // cycle to communicate with server
        while (true) {
            String str = null;
            try { str = in.readLine(); } catch (IOException e) { return; }
            String name;
            if (str.equals("NAMEREQUIRED")) {
                if (nickName == null) {
                    name = JOptionPane.showInputDialog(
                            chat,
                            "Enter a unique name: ",
                            "Name is obligatory. ",
                            JOptionPane.PLAIN_MESSAGE);
                } else {
                    name = nickName;
                }
                // sending name to the server
                out.println(name);
            } else if (str.equals("NAMEALREADYEXISTS")) {

                name = JOptionPane.showInputDialog(
                        chat,
                        "Enter another name, please. ",
                        "Sorry but this name already exists. ",
                        JOptionPane.WARNING_MESSAGE);

                out.println(name);

            } else if (str.startsWith("NAMEACCEPTED")) {

                messageField.setEditable(true);
                nameLabel.setText("You are " + str.substring(12));

            } else {
                textExchange.append(str + "\n");
            }
        }
    }
}

// button chat listener
class Listener implements ActionListener {

    @Override
    public void actionPerformed(ActionEvent e) {
        Client.out.println(Client.messageField.getText());
        Client.messageField.setText("");
    }
}


