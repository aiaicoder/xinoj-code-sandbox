package com.xin.ojcodesandbox.service.java;


import cn.hutool.dfa.FoundWord;
import cn.hutool.dfa.WordTree;
import com.xin.ojcodesandbox.model.ExecuteCodeRequest;
import com.xin.ojcodesandbox.model.ExecuteCodeResponse;
import com.xin.ojcodesandbox.model.ExecuteMessage;
import com.xin.ojcodesandbox.model.JudgeInfo;
import com.xin.ojcodesandbox.model.enums.JudgeInfoMessageEnum;
import com.xin.ojcodesandbox.model.enums.QuestionSubmitStatusEnum;
import com.xin.ojcodesandbox.service.CodeSandBox;
import com.xin.ojcodesandbox.service.CommonCodeSandBox;
import com.xin.ojcodesandbox.utils.ProcessUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static com.xin.ojcodesandbox.constant.CodeBlackList.PYTHON_BLACK_LIST;
import static com.xin.ojcodesandbox.constant.CodeBlackList.SENSITIVE_WORD_LIST;

/**
 * @author 15712
 */
@Slf4j
@Component
public abstract class JavaCodeSandBoxTemplate extends CommonCodeSandBox implements CodeSandBox {

    /**
     *
     */
    private static final String GLOBAL_CODE_DIR_NAME = "tmpCode";

    /**
     * 代码统一名称
     */
    private static final String GLOBAL_JAVA_CLASS_NAME = "Main.java";

    /**
     * 代码运行超时时间
     */
    private static final Long EXCESS_TIME = 10000L;

    /**
     * Java安全管理器类存放路径
     */
    private static final String SECURITY_MANAGER_PATH;

    /**
     * Java安全管理器类名
     */
    private static final String SECURITY_MANAGER_CLASS_NAME = "NowSecurityManager";

    /**
     * 使用hutool的工具类，字典树，存放黑名单
     */
    private static final WordTree WORD_TREE;

    static {
        WORD_TREE = new WordTree();
        WORD_TREE.addWords(SENSITIVE_WORD_LIST);
        // 初始安全配置文件路径
        SECURITY_MANAGER_PATH = System.getProperty("user.dir");
    }

    @Override
    public ExecuteCodeResponse executeCode(ExecuteCodeRequest executeCodeRequest) {
        String code = executeCodeRequest.getCode();
        List<String> inputList = executeCodeRequest.getInputList();
        String language = executeCodeRequest.getLanguage();
        System.out.println("当前操作系统：" + System.getProperty("os.name").toLowerCase());
        System.out.println("当前代码使用语言：" + language);
        //保存用户代码文件
        File userCodeFile = saveCodeToFile(code, language, GLOBAL_CODE_DIR_NAME, GLOBAL_JAVA_CLASS_NAME);

        //校验代码中的敏感代码
        FoundWord foundWord = WORD_TREE.matchWord(code);
        if (foundWord != null) {
            System.out.println("包含禁止词：" + foundWord.getFoundWord());
            // 返回错误信息
            return new ExecuteCodeResponse("包含禁止词：" + foundWord.getFoundWord(), QuestionSubmitStatusEnum.FAILED.getValue(), null, new JudgeInfo(JudgeInfoMessageEnum.DANGEROUS_OPERATION.getValue(), null, null));
        }

        //编译文件
        ExecuteMessage compileFileExecuteMessage = compileFile(userCodeFile);
        System.out.println("编译结果：" + compileFileExecuteMessage);

        if (compileFileExecuteMessage.getErrorMessage() != null) {
            // 返回编译错误信息
            return new ExecuteCodeResponse(compileFileExecuteMessage.getMessage(), QuestionSubmitStatusEnum.FAILED.getValue(), null, new JudgeInfo(compileFileExecuteMessage.getErrorMessage(), null, null));
        }

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
     * 2.编译代码
     *
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
                executeMessage.setExitCode(1);
                executeMessage.setMessage(JudgeInfoMessageEnum.COMPILE_ERROR.getText());
                executeMessage.setErrorMessage(JudgeInfoMessageEnum.COMPILE_ERROR.getValue());
            }
            //返回执行结果
            return executeMessage;
        } catch (Exception e) {
            // 未知错误
            ExecuteMessage executeMessage = new ExecuteMessage();
            executeMessage.setExitCode(1);
            executeMessage.setMessage(e.getMessage());
            executeMessage.setErrorMessage(JudgeInfoMessageEnum.SYSTEM_ERROR.getValue());
            return executeMessage;
        }
    }

    /**
     * 3.运行代码
     *
     * @param userCodeFile 用户代码文件
     * @param inputList    输入用例
     * @return
     */
    public List<ExecuteMessage> runFile(File userCodeFile, List<String> inputList) {
        String userCodeParentPath = userCodeFile.getParentFile().getAbsolutePath();
        List<ExecuteMessage> executeMessageList = new ArrayList<>();
        for (String input : inputList) {
            String runCmd = String.format("java -Xmx128m -Dfile.encoding=UTF-8 -cp %s Main %s", userCodeParentPath, input);
            String osName = System.getProperty("os.name").toLowerCase();
            if (osName.contains("nix") || osName.contains("nux")) {
                runCmd = String.format("java -Xmx128m -Dfile.encoding=UTF-8 -cp %s Main %s", userCodeParentPath, input);
            }
            try {
                Process runProcess = Runtime.getRuntime().exec(runCmd);
                // 安全控制：限制最大运行时间，超时控制
                new Thread(() -> {
                    try {
                        Thread.sleep(EXCESS_TIME);
                        runProcess.destroy();
                        System.out.println("超过程序最大运行时间，终止进程");
                    } catch (InterruptedException e) {
                        System.out.println("结束");
                    }
                }).start();
                ExecuteMessage executeMessage = ProcessUtils.runInteractProcessAndGetMessage(runProcess, input);
                System.out.println("本次运行结果：" + executeMessage);
                if (executeMessage.getExitCode() != 0) {
                    executeMessage.setExitCode(1);
                    executeMessage.setMessage(JudgeInfoMessageEnum.RUNTIME_ERROR.getText());
                    executeMessage.setErrorMessage(JudgeInfoMessageEnum.RUNTIME_ERROR.getValue());
                }
                executeMessageList.add(executeMessage);
            } catch (IOException e) {
                // 未知错误
                ExecuteMessage executeMessage = new ExecuteMessage();
                executeMessage.setExitCode(1);
                executeMessage.setMessage(e.getMessage());
                executeMessage.setErrorMessage(JudgeInfoMessageEnum.SYSTEM_ERROR.getValue());
                executeMessageList.add(executeMessage);
            }
        }
        return executeMessageList;
    }
}


