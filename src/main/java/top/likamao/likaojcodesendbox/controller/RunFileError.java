package top.likamao.likaojcodesendbox.controller;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class RunFileError {
    public static void main(String[] args) {
        Process process = null;
        try {
            String userDir = System.getProperty("user.dir");
            String filePath = userDir + "/src/main/resources/error.bat";
            process = Runtime.getRuntime().exec(filePath);
            process.waitFor();
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            StringBuilder output = new StringBuilder();
            String str;
            while(( str = reader.readLine())!= null) {
                output.append(str).append("\n");
            }
            System.out.println(output);
            reader.close();
            process.destroy();
            System.out.println("File executed successfully");
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } finally {
            process.destroy();
        }
    }
}
