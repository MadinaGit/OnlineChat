import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class Client {

    private final String hostName;
    private Socket socket;
    private final int port;
    private PrintWriter out;
    private BufferedReader in;

    public Client(String hostName, int port) {
        this.hostName = hostName;
        this.port = port;

    }

    public void clientConnection() throws IOException {
            socket = new Socket(hostName, port);
            out = new PrintWriter(socket.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        }


    public void sendMessage(String message) {
        out.println(message);
    }

    public String receiveMessage() throws IOException {
        return in.readLine();

    }

    public void finish() throws IOException {
        out.println("/exit");
        in.close();
        out.close();
        socket.close();
    }
}
