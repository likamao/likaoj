
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

public class Main {

    public static void main(String[] args) {
        String userDir = System.getProperty("user.dir");
        String filePath = userDir + "/src/main/resources/application.yml";
        try {
            List<String> configStr = Files.readAllLines(Paths.get(filePath));
            String config = String.join("\n", configStr);
            System.out.println(config);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
