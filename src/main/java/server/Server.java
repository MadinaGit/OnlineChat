import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Date;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;


public class Server {
    protected static final List<PrintWriter> clients = new CopyOnWriteArrayList<>();
    FileLogger fileLogger = new FileLogger("file.log");
    private final int port;


    public Server(int port) {
        this.port = port;
    }

    public void start() {

        try (ServerSocket serverSocket = new ServerSocket(port)) {

            System.out.println("Сервер запущен, номер порта " + port);

            while (true) {
                try {
                    Socket clientSocket = serverSocket.accept();
                    System.out.println("Новое соединение");

                    new UserManager(clientSocket).start();
                } catch (IOException e) {
                    System.out.println(e.getMessage());
                }
            }
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    private class UserManager extends Thread {
        private final Socket userSocket;
        private PrintWriter out;
        private BufferedReader in;
        private String userName;

        public UserManager(Socket socket) {
            this.userSocket = socket;
        }

        @Override
        public void run() {
            try {

                out = new PrintWriter(userSocket.getOutputStream(), true);
                in = new BufferedReader(new InputStreamReader(userSocket.getInputStream()));
                userName = in.readLine();
                synchronized (clients) {
                    clients.add(out);
                }

                fileLogger.logMessage("Пользователь " + userName + " подключился ");
                chatNotification(userName + " зашел в чат.");

                String userMessage;
                while (true) {
                    userMessage = in.readLine();
                    if (userMessage == null || userMessage.equals("/exit")) {
                        fileLogger.logMessage("Пользователь " + userName + " отключился.");
                        chatNotification(userName + "покинул чат");
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
                    if (userName != null) {
                        synchronized (clients) {
                            clients.remove(out);
                        }
                        chatNotification(userName + " вышел из чата.");
                    }
                    userSocket.close();
                } catch (IOException e) {
                    System.out.println(e.getMessage());
                }
            }
        }


        private void chatNotification(String message) {
            for (PrintWriter client : clients) {
                if (client != this.out) {
                    client.println(new Date() + " " + message);
                }
            }
        }
    }
}
