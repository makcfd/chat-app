import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ServerTest {

    private Server server;
    private Client client;

    @BeforeEach
    public void setUp() {
        server = new Server();
        client = new Client("localhost", 9800, "nonono");
    }

    @Test
    void serverReturnsReposnse() throws InterruptedException {
        // given
        String expectedStatus = "200 OK";
        // when
        server.start();
        Thread.sleep(500);
        Thread t1 = new Thread(client);
        t1.start();
        Thread.sleep(500);
        String result = server.getStatus();
        // then
        assertEquals(expectedStatus, result);
    }

}
