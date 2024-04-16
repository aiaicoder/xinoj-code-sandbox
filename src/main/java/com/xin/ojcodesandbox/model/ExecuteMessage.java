package com.xin.ojcodesandbox.model;

import lombok.Data;

/**
 * @author 15712
 * 编译信息
 */
@Data
public class ExecuteMessage {
    /**
     * 退出码
     */
    private Integer exitCode;
    /**
     * 错误信息
     */
    private String errorMessage;

    /**
     * 正常输出信息
     */
    private String message;

    /**
     * 执行内存
     */
    private Long memory;

    /**
     * 程序执行时间
     */
    private Long time;
}
