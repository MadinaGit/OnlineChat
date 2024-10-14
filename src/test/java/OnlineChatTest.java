import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class OnlineChatTest {
    private static Client client;
static ServerPort serverPort = new ServerPortImp();

    @BeforeAll
    public static void configuration() {

        Thread server = new Thread(() -> {
            try {
               Server server1 = new Server(serverPort.getPort());
               server1.start();
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        server.start();


        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @AfterAll
    public static void finish() {
        if (client != null) {
            try {
                client.finish();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Test
    public void testChat() {
        try {
            String hostName = "localhost";
            ServerPort serverPort = new ServerPortImp();
            int port = serverPort.getPort();
            client = new Client(hostName, port);
            client.clientConnection();
            client.sendMessage("TestClient");
            String testMessage = "Всем привет!";
            client.sendMessage(testMessage);
            String responseMessage = client.receiveMessage();
            System.out.println("Ответ сервера: " + responseMessage);

            assertTrue(responseMessage.contains("TestClient зашел в чат"), "Ответ сервера не содержит ожидаемое сообщение.");

        } catch (IOException e) {
            e.printStackTrace();

        }
    }
}

