import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;



public class Server {
    private static final List<PrintWriter> clients = new ArrayList<>();
    private static String userName;

    public static void main(String[] args) {
        ServerPort serverPort = new ServerPortImp();

        try (ServerSocket serverSocket = new ServerSocket(serverPort.getPort())) {
            System.out.println("Сервер запущен, номер порта " + serverPort.getPort());
            while (true) {
                try {
                    Socket clientSocket = serverSocket.accept();
                    System.out.println("Новое соединение");

                    PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
                    BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                    clients.add(out);

                    new Thread(() -> {
                        try {
                            userName = in.readLine();
                            System.out.println("Пользователь " + userName + " подключился.");
                            chatNotification(userName + " зашел в чат.");
                            String userMessage;
                            FileLogger fileLogger = new FileLogger("file.log");
                            while (true) {
                                userMessage = in.readLine();
                                if (userMessage.equals("/exit")) {
                                    System.out.println("Пользователь " + userName + " отключился.");
                                    chatNotification(userName + " покинул чат.");
                                    break;
                                }
                                String newMessage = userName + ": " + userMessage;
                                fileLogger.logMessage(newMessage);
                                chatNotification(newMessage);

                            }
                        } catch (IOException e) {
                            System.out.println(e.getMessage());
                        } finally {
                            try {
                                clientSocket.close();
                            } catch (IOException e) {
                                System.out.println(e.getMessage());
                            }
                        }
                    }).start();
                } catch (IOException e) {
                    System.out.println(e.getMessage());
                }
            }
        } catch (
                IOException ex) {
           ex.printStackTrace();
        }
    }

    private static synchronized void chatNotification(String s) {
        for (PrintWriter client : clients) {
            client.println(s);
        }
    }

}
