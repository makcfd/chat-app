import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class Main {

    public static void main(String[] args) throws InterruptedException {

        Properties prop = new Properties();
        String ipAddress = null;
        int port = 0;
        String name = "Nonono";

        try {
            //load a properties file from class path
            prop.load(new FileInputStream("/Users/Armi/IdeaProjects/ChatModified/src/main/resources/config.properties"));
            //get the property value and print it out
            ipAddress = prop.getProperty("ipAddress");
            port = Integer.parseInt(prop.getProperty("port"));

        } catch (IOException ex) { ex.printStackTrace(); }

        Server server = new Server();
        server.start();

        Server server2 = new Server();
        server2.start();

        //Thread.sleep(1000);
        Client client = new Client(ipAddress, port, name);
        Thread t1 = new Thread(client);
        t1.start();

        Thread.sleep(1000);
        Client client2 = new Client(ipAddress, port);
        Thread t2 = new Thread(client2);
        t2.start();

    }
}
