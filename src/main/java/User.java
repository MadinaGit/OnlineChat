import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class User {
    public static void main(String[] args) {
        ServerPort serverPort = new ServerPortImp();

        FileLogger fileLogger = new FileLogger("file.log");
        try (Socket clientSocket = new Socket("localhost", serverPort.getPort());
             PrintWriter writer = new PrintWriter(clientSocket.getOutputStream(), true);
             BufferedReader reader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()))) {

            Scanner scanner = new Scanner(System.in);
            System.out.print("Пожалуйста, представьтесь: ");
            String userName = scanner.nextLine();
            fileLogger.logMessage(userName + " подключился к чату");
            System.out.println("Добро пожаловать в чат, " + userName + ", можете начинать общение, для выхода из чата напишите /exit");
            writer.println(userName);

            new Thread(() -> {
                String serverMessage;
                try {
                    while ((serverMessage = reader.readLine()) != null) {
                        System.out.println(serverMessage);
                        fileLogger.logMessage("Сообщение от сервера: " + serverMessage);
                    }
                } catch (IOException e) {
                    System.err.println("Ошибка при чтении из сервера: " + e.getMessage());
                }
            }).start();


            String userMessage;
            while (true) {
                userMessage = scanner.nextLine();
                if (userMessage.equals("/exit")) {
                    writer.println("/exit");
                    fileLogger.logMessage(userName + " вышел из чата");
                    break;
                }
                writer.println(userMessage);
            }

            System.out.println("До свидания! Ждем Вас снова :)");

        } catch (IOException e) {
            throw new RuntimeException("Ошибка клиента: " + e.getMessage());
        }

    }
}
