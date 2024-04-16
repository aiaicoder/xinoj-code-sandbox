package com.xin.ojcodesandbox;


import cn.hutool.core.io.FileUtil;
import cn.hutool.core.lang.UUID;
import cn.hutool.core.util.StrUtil;
import com.xin.ojcodesandbox.model.ExecuteCodeRequest;
import com.xin.ojcodesandbox.model.ExecuteCodeResponse;
import com.xin.ojcodesandbox.model.ExecuteMessage;
import com.xin.ojcodesandbox.model.JudgeInfo;
import com.xin.ojcodesandbox.utils.ProcessUtils;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author 15712
 */
@Slf4j
public abstract class JavaCodeSandBoxTemplate implements CodeSandBox {

    private static final String GLOBAL_CODE_DIR_NAME = "tmpCode";

    private static final String GLOBAL_JAVA_CLASS_NAME = "Main.java";

    private static final Long EXCESS_TIME = 5000L;

    @Override
    public ExecuteCodeResponse executeCode(ExecuteCodeRequest executeCodeRequest) {
        String code = executeCodeRequest.getCode();
        List<String> inputList = executeCodeRequest.getInputList();
        String language = executeCodeRequest.getLanguage();
        File userCodeFile = saveCodeToFile(code, language);

        //编译文件
        ExecuteMessage complieExecuteMessage = compileFile(userCodeFile);
        System.out.println(complieExecuteMessage);

        //运行文件
        List<ExecuteMessage> executeMessageList = runFile(userCodeFile, inputList);

        //收集结果
        ExecuteCodeResponse executeCodeResponse = getOutputResponse(executeMessageList);

        //删除文件
        boolean b = delCodeFile(userCodeFile);
        if (!b) {
            log.info("删除文件失败{}", userCodeFile);
        }
        return executeCodeResponse;
    }

    /**
     * 1.保存用户代码
     *
     * @param code     代码
     * @param language 语言
     * @return
     */
    public File saveCodeToFile(String code, String language) {
        String projectPath = System.getProperty("user.dir");
        String globalCodePathName = projectPath + File.separator + GLOBAL_CODE_DIR_NAME;
        if (!FileUtil.exist(globalCodePathName)) {
            FileUtil.mkdir(globalCodePathName);
        }
        //把用户代码隔离
        String userCodeParentPath = globalCodePathName + File.separator + UUID.randomUUID();
        String userCodePath = userCodeParentPath + File.separator + GLOBAL_JAVA_CLASS_NAME;
        return FileUtil.writeUtf8String(code, userCodePath);
    }

    /**
     * 2.编译代码
     * @param userCodeFile 用户代码文件
     * @return
     */
    public ExecuteMessage compileFile(File userCodeFile) {
        //2.执行编译程序
        String compileCmd = String.format("javac -encoding utf-8 %s", userCodeFile.getAbsolutePath());
        try {
            Process complieProcess = Runtime.getRuntime().exec(compileCmd);
            ExecuteMessage executeMessage = ProcessUtils.runProcessAndGetMessage(complieProcess, "编译");
            if (executeMessage.getExitCode() != 0) {
                throw new RuntimeException(executeMessage.getMessage());
            }
            //返回执行结果
            return executeMessage;
        } catch (Exception e) {
            //返回错误的响应
            throw new RuntimeException(e);
        }
    }

    /**
     * 3.运行代码
     * @param userCodeFile 用户代码文件
     * @param inputList 输入用例
     * @return
     */
    public List<ExecuteMessage> runFile(File userCodeFile, List<String> inputList) {
        //3.执行编译好的class文件
        List<ExecuteMessage> executeMessageList = new ArrayList<>();
        for (String input : inputList) {
            String runCmd = String.format("java -Xmx256m -Dfile.encoding=UTF-8 -cp %s Main %s", userCodeFile.getAbsolutePath(), input);
            try {
                Process runProcess = Runtime.getRuntime().exec(runCmd);
                new Thread(() -> {
                    try {
                        Thread.sleep(EXCESS_TIME);
                        runProcess.destroy();
                        System.out.println("你超时了");
                    } catch (InterruptedException e) {
                        System.out.println("结束");
                    }
                }).start();
                ExecuteMessage executeMessage = ProcessUtils.runProcessAndGetMessage(runProcess, "运行");
                executeMessageList.add(executeMessage);
            } catch (IOException e) {
                throw new RuntimeException("运行错误");
            }
        }
        return executeMessageList;
    }


    /**
     * 收集运行结果
     * @param executeMessageList 执行结果列表
     * @return
     */
    public ExecuteCodeResponse getOutputResponse(List<ExecuteMessage> executeMessageList) {
        //4. 收集整理输出结果
        //准备返回信息对象
        ExecuteCodeResponse executeCodeResponse = new ExecuteCodeResponse();
        List<String> outputList = new ArrayList<>();
        long maxTime = 0;
        for (ExecuteMessage executeMessage : executeMessageList) {
            String errorMessage = executeMessage.getErrorMessage();
            if (StrUtil.isNotBlank(errorMessage)) {
                outputList.add(executeMessage.getMessage());
                //执行中出现错误
                executeCodeResponse.setStatus(3);
                break;
            }
            Long time = executeMessage.getTime();

            if (time != null) {
                maxTime = Math.max(time, maxTime);
            }
            //如果没有错误信息就正常添加
            outputList.add(executeMessage.getMessage());
        }
        //没有错误信息
        if (outputList.size() == executeMessageList.size()) {
            executeCodeResponse.setStatus(1);
        }
        executeCodeResponse.setOutputList(outputList);
        //正常运行
        JudgeInfo judgeInfo = new JudgeInfo();
//        judgeInfo.setMemory(0L);
        judgeInfo.setTime(maxTime);
        executeCodeResponse.setJudgeInfo(judgeInfo);
        return executeCodeResponse;
    }

    /**
     * 删除文件
     * @param userCodeFile 用户代码文件
     * @return
     */
    public boolean delCodeFile(File userCodeFile) {
        if (userCodeFile.getParentFile() != null) {
            boolean del = FileUtil.del(userCodeFile.getParentFile().getAbsolutePath());
            System.out.println("删除" + (del ? "成功" : "失败"));
            return del;
        }
        return true;
    }


    /**
     * 错误响应，当执行或者编译错误的时候直接返回
     *
     * @param e
     * @return
     */
    private ExecuteCodeResponse getErrorResponse(Throwable e) {
        ExecuteCodeResponse executeCodeResponse = new ExecuteCodeResponse();
        executeCodeResponse.setMessage(e.getMessage());
        //表示代码沙箱错误
        executeCodeResponse.setStatus(2);
        executeCodeResponse.setOutputList(new ArrayList<>());
        executeCodeResponse.setJudgeInfo(new JudgeInfo());
        return executeCodeResponse;
    }
}


