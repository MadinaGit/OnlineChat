import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class FileLogger {
    private final String logFile;

    public FileLogger(String logFile) {
        this.logFile = logFile;
    }

    public void logMessage(String message) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(logFile, true))) {
            String messageDate = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss").format(new Date());
            writer.write(messageDate + " " + message);
            writer.newLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
