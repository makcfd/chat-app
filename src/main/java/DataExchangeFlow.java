import java.io.*;
import java.net.Socket;

class DataExchangeFlow extends Thread {

    // defining elements of a chat on server side for one client
    Socket socket;
    BufferedReader in;
    PrintWriter out;
    String name;
    PrintWriter pw;
    static FileWriter fw;
    static BufferedWriter bw;

    public DataExchangeFlow(Socket socket) throws IOException {
        this.socket = socket;
        // file to write and claim to append info
        fw = new FileWriter("/Users/Armi/IdeaProjects/Chat-app/src/logs.txt", true);

        // FileWriter write one character but we need to write a string -> use BufferWriter
        bw = new BufferedWriter(fw);
        // write to the file
        pw = new PrintWriter(bw, true);

    }

    @Override
    public void run() {

        try {
            // in for receiving data
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            // out for sending data
            out = new PrintWriter(socket.getOutputStream(), true);


            int count = 0;
            // sending messages to client UI
            while (true) {
                if (count > 0) {
                    out.println("NAMEALREADYEXISTS");
                } else {
                    // first message to be send from server to client
                    out.println("NAMEREQUIRED");
                }
                name = in.readLine();

                if (name == null) {
                    return;
                }

                if (!Server.clients.contains(name)) {
                    Server.clients.add(name);
                    break;
                }
                count++;
            }
            out.println("NAMEACCEPTED" + name);
            Server.printWriters.add(out);

            // if we get a message we send it to other users
            while (true) {

                String message = in.readLine();
                if (message == null) {
                    return;
                }

                // logging messages to a log file
                pw.println(name + ": " + message);

                for (PrintWriter writer : Server.printWriters) {
                    writer.println(name + ": " + message);
                }
            }

        } catch (IOException e) {
            System.out.println(e);
        }
    }
}