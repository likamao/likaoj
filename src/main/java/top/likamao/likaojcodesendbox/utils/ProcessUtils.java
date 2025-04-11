package top.likamao.likaojcodesendbox.utils;

import cn.hutool.core.util.StrUtil;
import org.springframework.util.StopWatch;
import top.likamao.likaojcodesendbox.model.ExecuteMessage;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

public class ProcessUtils {

    /**
     * 运行命令行程序
     *
     * @param compileProcess 编译进程
     * @param operationName  操作名称
     * @return
     */
    public static ExecuteMessage runCommand(Process compileProcess, String operationName) {
        try {
            ExecuteMessage executeMessage = new ExecuteMessage();
            StopWatch stopWatch = new StopWatch();
            stopWatch.start();
            int exitValue = compileProcess.waitFor();
            executeMessage.setExitValue(exitValue);
            if (exitValue != 0) {
                BufferedReader compileProcessResultReader = new BufferedReader(new InputStreamReader(compileProcess.getInputStream()));
                String compileProcessResultLine;
                StringBuilder compileProcessResultBuilder = new StringBuilder();
                while ((compileProcessResultLine = compileProcessResultReader.readLine()) != null) {
                    compileProcessResultBuilder.append(compileProcessResultLine);
                }

                BufferedReader compileProcessErrorReader = new BufferedReader(new InputStreamReader(compileProcess.getErrorStream()));
                String compileProcessErrorLine;
                StringBuilder compileProcessErrorBuilder = new StringBuilder();
                while ((compileProcessErrorLine = compileProcessErrorReader.readLine()) != null) {
                    compileProcessErrorBuilder.append(compileProcessErrorLine);
                }
                System.out.printf("%s 失败, 编译信息:%s, 错误信息:%s%n", operationName, compileProcessResultBuilder, compileProcessErrorBuilder);

                executeMessage.setMessage(compileProcessResultBuilder.toString());
                executeMessage.setErrorMessage(compileProcessErrorBuilder.toString());

            } else {
                BufferedReader compileProcessResultReader = new BufferedReader(new InputStreamReader(compileProcess.getInputStream()));
                String compileProcessResultLine;
                StringBuilder compileProcessResultBuilder = new StringBuilder();
                while ((compileProcessResultLine = compileProcessResultReader.readLine()) != null) {
                    compileProcessResultBuilder.append(compileProcessResultLine);
                }
                System.out.printf("%s 成功, 编译信息:%s%n", operationName, compileProcessResultBuilder);
                executeMessage.setMessage(compileProcessResultBuilder.toString());
            }
            stopWatch.stop();
            executeMessage.setExecuteTime(stopWatch.getTotalTimeMillis());
            return executeMessage;
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 运行命令行程序
     *
     * @param compileProcess 编译进程
     * @param operationName  操作名称
     * @return
     */
    public static ExecuteMessage runInterCommand(Process compileProcess, String operationName, String args) {
        try {
            ExecuteMessage executeMessage = new ExecuteMessage();
            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(compileProcess.getOutputStream());
            String[] argsArray = args.split(StrUtil.SPACE);
            outputStreamWriter.write(StrUtil.join("\n", argsArray) + "\n");
            outputStreamWriter.flush();
            outputStreamWriter.close();

            int exitValue = compileProcess.waitFor();
            executeMessage.setExitValue(exitValue);
            if (exitValue != 0) {
                BufferedReader compileProcessResultReader = new BufferedReader(new InputStreamReader(compileProcess.getInputStream()));
                String compileProcessResultLine;
                StringBuilder compileProcessResultBuilder = new StringBuilder();
                while ((compileProcessResultLine = compileProcessResultReader.readLine()) != null) {
                    compileProcessResultBuilder.append(compileProcessResultLine);
                }

                BufferedReader compileProcessErrorReader = new BufferedReader(new InputStreamReader(compileProcess.getErrorStream()));
                String compileProcessErrorLine;
                StringBuilder compileProcessErrorBuilder = new StringBuilder();
                while ((compileProcessErrorLine = compileProcessErrorReader.readLine()) != null) {
                    compileProcessErrorBuilder.append(compileProcessErrorLine);
                }
                System.out.printf("%s 失败, 编译信息:%s, 错误信息:%s%n", operationName, compileProcessResultBuilder, compileProcessErrorBuilder);

                executeMessage.setMessage(compileProcessResultBuilder.toString());
                executeMessage.setErrorMessage(compileProcessErrorBuilder.toString());

            } else {
                BufferedReader compileProcessResultReader = new BufferedReader(new InputStreamReader(compileProcess.getInputStream()));
                String compileProcessResultLine;
                StringBuilder compileProcessResultBuilder = new StringBuilder();
                while ((compileProcessResultLine = compileProcessResultReader.readLine()) != null) {
                    compileProcessResultBuilder.append(compileProcessResultLine);
                }
                System.out.printf("%s 成功, 编译信息:%s%n", operationName, compileProcessResultBuilder);
                executeMessage.setMessage(compileProcessResultBuilder.toString());
            }
            return executeMessage;
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return null;
    }
}
