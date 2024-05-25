package com.xin.ojcodesandbox.service.cpp;

import com.xin.ojcodesandbox.model.ExecuteCodeRequest;
import com.xin.ojcodesandbox.model.ExecuteCodeResponse;
import com.xin.ojcodesandbox.service.c.CCodeSandboxTemplate;
import org.springframework.stereotype.Component;


/**
 * Java 原生代码沙箱实现（直接复用模板方法）
 *
 * @author 15712
 */
@Component
public class CppNativeCodeSandbox extends CppCodeSandboxTemplate {

    /**
     * 执行代码
     *
     * @return
     */
    @Override
    public ExecuteCodeResponse executeCode(ExecuteCodeRequest executeCodeRequest) {
        return super.executeCode(executeCodeRequest);
    }
}

