package com.xin.ojcodesandbox;

import com.xin.ojcodesandbox.model.ExecuteCodeRequest;
import com.xin.ojcodesandbox.model.ExecuteCodeResponse;
import org.springframework.stereotype.Component;


/**
 * Java 原生代码沙箱实现（直接复用模板方法）
 */
@Component
public class JavaNativeCodeSandbox extends JavaCodeSandBoxTemplate {

    /**
     * 执行代码
     * @param executeCodeRequest
     * @return
     */
    @Override
    public ExecuteCodeResponse executeCode(ExecuteCodeRequest executeCodeRequest) {
        return super.executeCode(executeCodeRequest);
    }
}

