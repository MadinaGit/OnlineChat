import java.io.BufferedReader;
import java.io.FileReader;

public class ServerPortImp implements ServerPort {
    protected int PORT;

    public ServerPortImp() {
        portReader();
    }

    protected void portReader() {
        try (
                BufferedReader reader = new BufferedReader(new FileReader("settings.txt"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.startsWith("PORT=")) {
                    PORT = Integer.parseInt(line.substring(5).trim());
                    return;
                }
            }
            System.out.println("PORT не найден в settings.txt.");
        } catch (Exception e) {
            System.out.println("Ошибка: " + e.getMessage());
        }
    }

    @Override
    public int getPort() {
        return PORT;
    }
}
