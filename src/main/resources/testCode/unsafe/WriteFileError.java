import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        try {
            String userDir = System.getProperty("user.dir");
            String filePath = userDir + "/src/main/resources/error.bat";
            String cmd = "java -version ";
            Files.write(Paths.get(filePath), List.of(cmd));
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
