package com.xin.ojcodesandbox.service;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.lang.UUID;
import cn.hutool.core.util.StrUtil;
import com.xin.ojcodesandbox.model.ExecuteCodeResponse;
import com.xin.ojcodesandbox.model.ExecuteMessage;
import com.xin.ojcodesandbox.model.JudgeInfo;
import com.xin.ojcodesandbox.model.enums.QuestionSubmitStatusEnum;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * @author 15712
 */
public abstract class CommonCodeSandBox implements CodeSandBox {

    /**
     * 1.保存用户代码
     *
     * @param code     代码
     * @param language 语言
     * @return
     */
    public File saveCodeToFile(String code, String language , String globalCodePath, String fileName) {
        String projectPath = System.getProperty("user.dir");
        String globalCodePathName = projectPath + File.separator + globalCodePath;
        if (!FileUtil.exist(globalCodePathName)) {
            FileUtil.mkdir(globalCodePathName);
        }
        //把用户代码隔离
        String userCodeParentPath = globalCodePathName + File.separator + UUID.randomUUID();
        String userCodePath = userCodeParentPath + File.separator + fileName;
        return FileUtil.writeUtf8String(code, userCodePath);
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
        long maxMemory = 0;
        for (ExecuteMessage executeMessage : executeMessageList) {
            String errorMessage = executeMessage.getErrorMessage();
            if (StrUtil.isNotBlank(errorMessage)) {
                outputList.add(executeMessage.getMessage());
                //执行中出现错误
                // 用户提交的代码执行中存在错误,直接返回
                executeCodeResponse.setStatus(QuestionSubmitStatusEnum.FAILED.getValue());
                executeCodeResponse.setJudgeInfo(new JudgeInfo(errorMessage, null, null));
                break;
            }
            //如果没有错误信息就正常添加
            outputList.add(executeMessage.getMessage());
            Long time = executeMessage.getTime();
            if (time != null) {
                maxTime = Math.max(maxTime, time);

            }
            Long memory = executeMessage.getMemory();
            if (memory != null)
            {
                maxMemory += memory;
            }
        }
        //没有错误信息
        if (outputList.size() == executeMessageList.size()) {
            executeCodeResponse.setStatus(QuestionSubmitStatusEnum.SUCCEED.getValue());
            executeCodeResponse.setMessage(QuestionSubmitStatusEnum.SUCCEED.getText());
        }
        executeCodeResponse.setOutputList(outputList);
        //正常运行
        JudgeInfo judgeInfo = new JudgeInfo();
        judgeInfo.setMemory(maxMemory);
        judgeInfo.setTime(maxTime);
        // 运行正常完成则不设置message，交由判题机判题
        executeCodeResponse.setJudgeInfo(judgeInfo);
        System.out.println(executeCodeResponse);
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

}
