import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class Server extends Thread {

    static ArrayList<String> clients = new ArrayList<String>();
    // we have to share messages among all users
    // to do that we have to have printWriters of all users
    static ArrayList<PrintWriter> printWriters = new ArrayList<PrintWriter>();
    private static String status = "NOTWORKING";

    @Override
    public void run() {
        System.out.println("Waiting for a clients...");
        ServerSocket ss = null;

        try { ss = new ServerSocket(9800); } catch (IOException e) { return; }

        while (true) {
            Socket soc; // accepting client connections and returning socket object which will be used to communicate
            try { soc = ss.accept(); status = ss.toString(); } catch (IOException e) { return; }

            System.out.println("New connection has been established");
            // we create a thread object as ConversationHandler
            DataExchangeFlow flow = null;
            try { flow = new DataExchangeFlow(soc); } catch (IOException e) { return; }

            flow.start();
            status = "200 OK";
        }
    }

    public static String getStatus() {
        return status;
    }
}




