package com.xin.ojcodesandbox.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author 15712
 * 判题的响应
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ExecuteCodeResponse {

    /**
     * 接口信息
     */
    private String message;

    /**
     * 判题状态
     */
    private Integer status;

    /**
     * 判题配置
     */
    private List<String> outputList;


    /**
     * 返回判题信息
     */
    private JudgeInfo judgeInfo;

}
