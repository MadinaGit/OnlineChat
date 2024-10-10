import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MultiClientTest {
    protected static List<String> names = new ArrayList<>();
    protected static String hostName = "localhost";


    public static void main(String[] args) {
        names.add("Тимофей");
        names.add("Алена");
        names.add("Екатерина");
        names.add("Алексей");
        names.add("Виктория");
        ServerPort serverPort = new ServerPortImp();
        int port = serverPort.getPort();
        for (int i = 0; i < names.size(); i++) {
            int finalI = i;
            new Thread(() -> {
                String clientName = names.get(finalI);

                Client client = new Client(hostName, port);

                try {
                    client.clientConnection();
                    String message = "Всем привет!";
                    client.sendMessage(clientName);
                    System.out.println("Пользователь " + clientName + " отправил " + message);

                    String response = client.receiveMessage();
                    System.out.println("Пользователь " + clientName + " получил " + response);

                    client.finish();
                } catch (IOException e) {
                    System.out.println("Ошибка подключения пользователя " + clientName + e.getMessage());
                }
            }).start();
        }
    }
}
