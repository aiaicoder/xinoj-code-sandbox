package com.xin.ojcodesandbox.utils;


import com.xin.ojcodesandbox.model.ExecuteMessage;
import lombok.extern.slf4j.Slf4j;
import org.apache.tomcat.util.buf.StringUtils;
import org.springframework.util.StopWatch;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

/**
 * @author 15712
 * 进程工具类
 */
@Slf4j
public class ProcessUtils {


    /**
     * @param runProcess
     * @param opName
     * @return
     */
    public static ExecuteMessage runProcessAndGetMessage(Process runProcess, String opName) {
        ExecuteMessage executeMessage = new ExecuteMessage();
        //记录程序还未执行的内存使用量
        long initialMemory = getUsedMemory();
        try {
            StopWatch stopWatch = new StopWatch();
            stopWatch.start();
            //等待程序执行获取错误码
            int exitCode = runProcess.waitFor();
            executeMessage.setExitCode(exitCode);
            //正常退出
            if (exitCode == 0) {
                System.out.println(opName + "成功");
                //运行正常输出流
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(runProcess.getInputStream(), StandardCharsets.UTF_8));
                List<String> outputStrList = new ArrayList<>();
                //进行逐行读取
                String complieOutLine;
                while ((complieOutLine = bufferedReader.readLine()) != null) {
                    outputStrList.add(complieOutLine);
                }
                executeMessage.setMessage(StringUtils.join(outputStrList, '\n'));
            } else {
                //异常退出
                System.out.println(opName + "失败：错误码：" + exitCode);
                //运行正常输出流
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(runProcess.getInputStream(), StandardCharsets.UTF_8));
                List<String> outputStrList = new ArrayList<>();
                //进行逐行读取
                String complieOutLine;
                while ((complieOutLine = bufferedReader.readLine()) != null) {
                    outputStrList.add(complieOutLine);
                }
                executeMessage.setMessage(StringUtils.join(outputStrList, '\n'));
                //分批获取错误输出
                BufferedReader bufferedReaderError = new BufferedReader(new InputStreamReader(runProcess.getErrorStream(), StandardCharsets.UTF_8));
                //逐行读取
                List<String> errorOutputStrList = new ArrayList<>();
                String complieOutLineError;
                while ((complieOutLineError = bufferedReaderError.readLine()) != null) {
                    errorOutputStrList.add(complieOutLineError);
                }
                executeMessage.setErrorMessage(StringUtils.join(errorOutputStrList, '\n'));
            }
            long finalMemory = getUsedMemory();
            // 计算内存使用量，单位字节，转换成kb需要除以1024
            long memoryUsage = finalMemory - initialMemory;
            stopWatch.stop();
            executeMessage.setTime(stopWatch.getTotalTimeMillis());
            executeMessage.setMemory(memoryUsage);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return executeMessage;
    }

    /**
     * 执行交互式进程并获取信息
     *
     * @param runProcess
     * @param args
     * @return
     */
    public static ExecuteMessage runInteractProcessAndGetMessage(Process runProcess, String args) throws IOException {
        // 向控制台输入程序
        ExecuteMessage executeMessage = new ExecuteMessage();
        //记录程序还未执行的内存使用量
        long initialMemory = getUsedMemory();
        try (OutputStreamWriter outputStreamWriter = new OutputStreamWriter(runProcess.getOutputStream())) {
            String[] arguments = args.split(" ");
            for (String arg : arguments) {
                outputStreamWriter.write(arg);
                outputStreamWriter.write("\n");
            }
            // 相当于按了回车，执行输入的发送
            outputStreamWriter.flush();
            //记录程序开始执行时间
            StopWatch stopWatch = new StopWatch();
            stopWatch.start();
            int exitCode = runProcess.waitFor();
            stopWatch.stop();
            executeMessage.setExitCode(exitCode);
            if (exitCode == 0) {
                // 分批获取进程的正常输出
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(runProcess.getInputStream()));
                List<String> outputStrList = new ArrayList<>();
                // 逐行读取
                String compileOutputLine;
                while ((compileOutputLine = bufferedReader.readLine()) != null) {
                    outputStrList.add(compileOutputLine);
                }
                executeMessage.setMessage(StringUtils.join(outputStrList, '\n'));
            } else {
                //异常退出
                System.out.println("失败：错误码：" + exitCode);
                //运行正常输出流
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(runProcess.getInputStream(), StandardCharsets.UTF_8));
                List<String> outputStrList = new ArrayList<>();
                //进行逐行读取
                String complieOutLine;
                while ((complieOutLine = bufferedReader.readLine()) != null) {
                    outputStrList.add(complieOutLine);
                }
                executeMessage.setErrorMessage(StringUtils.join(outputStrList, '\n'));
                //分批获取错误输出
                BufferedReader bufferedReaderError = new BufferedReader(new InputStreamReader(runProcess.getErrorStream(), StandardCharsets.UTF_8));
                //逐行读取
                List<String> errorOutputStrList = new ArrayList<>();
                String complieOutLineError;
                while ((complieOutLineError = bufferedReaderError.readLine()) != null) {
                    errorOutputStrList.add(complieOutLineError);
                }
                executeMessage.setErrorMessage(StringUtils.join(errorOutputStrList, '\n'));

            }
            long finalMemory = getUsedMemory();
            // 计算内存使用量，单位字节，转换成kb需要除以1024
            long memoryUsage = finalMemory - initialMemory;
            executeMessage.setTime(stopWatch.getTotalTimeMillis());
            executeMessage.setMemory(memoryUsage);
        } catch (Exception e) {
            // 使用日志框架记录异常
            log.error("执行交互式进程出错", e);
        } finally {
            // 记得资源的释放，否则会卡死
            runProcess.destroy();
        }
        return executeMessage;
    }


    public static ExecuteMessage runInteractProcessAndGetMessageAnother(Process runProcess, String args) {
        ExecuteMessage executeMessage = new ExecuteMessage();

        try (OutputStream outputStream = runProcess.getOutputStream(); OutputStreamWriter outputStreamWriter = new OutputStreamWriter(outputStream); InputStream inputStream = runProcess.getInputStream(); BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream))) {
            // Write arguments to the process's output stream
            String[] arguments = args.split(" ");
            for (String arg : arguments) {
                outputStreamWriter.write(arg);
                outputStreamWriter.write("\n");
            }
            outputStreamWriter.flush();

            // Read output from the process's input stream
            StringBuilder compileOutputStringBuilder = new StringBuilder();
            String compileOutputLine;
            while ((compileOutputLine = bufferedReader.readLine()) != null) {
                compileOutputStringBuilder.append(compileOutputLine).append("\n");
            }
            executeMessage.setMessage(compileOutputStringBuilder.toString());

        } catch (IOException e) {
            // Handle potential I/O errors here
            e.printStackTrace();
            executeMessage.setErrorMessage(e.getMessage());
        } finally {
            // Ensure that the process is destroyed
            runProcess.destroy();
        }
        return executeMessage;
    }

    /**
     * 获取当前已使用的内存量
     * 单位是byte
     *
     * @return
     */
    public static long getUsedMemory() {
        Runtime runtime = Runtime.getRuntime();
        return runtime.totalMemory() - runtime.freeMemory();
    }

}
