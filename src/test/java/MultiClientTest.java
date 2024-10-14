import java.io.IOException;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class MultiClientTest {
    protected static List<String> names = new CopyOnWriteArrayList<>();
    protected static String hostName = "localhost";
    static ServerPort serverPort = new ServerPortImp();

    public static void main(String[] args) {
        names.add("Тимофей");
        names.add("Алена");
        names.add("Екатерина");
        names.add("Алексей");
        names.add("Виктория");
        int port = serverPort.getPort();
        Server server = new Server(port);
        new Thread(server::start).start();
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        for (String userName : names) {
            new Thread(() -> {

                Client client = new Client(hostName, port);

                try {
                    client.clientConnection();
                    String message = "Всем привет!";
                    client.sendMessage(userName);

                    System.out.println("Пользователь " + userName + " отправил " + message);

                    String response = client.receiveMessage();
                    System.out.println("Пользователь " + userName + " получил " + response);


                } catch (IOException e) {
                    System.out.println("Ошибка подключения пользователя " + userName + e.getMessage());
                } finally {
                    try {
                        client.finish();
                    } catch (IOException e) {
                        System.out.println("Ошибка при завершении соединения пользователя" + e.getMessage());
                    }
                }
            }).start();
        }
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}
