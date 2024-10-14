import addition.ServerPort;
import addition.ServerPortImp;
import server.Server;
import user.User;

public class Main {
    public static void main(String[] args) {

        ServerPort serverPort = new ServerPortImp();
        Server server = new Server(serverPort.getPort());
        new Thread(server::start).start();
        try {

            Thread.sleep(1000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        User user = new User(serverPort.getPort());
        user.start();
    }
}