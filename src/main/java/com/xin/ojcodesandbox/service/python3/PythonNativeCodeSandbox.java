package com.xin.ojcodesandbox.service.python3;

import com.xin.ojcodesandbox.model.ExecuteCodeRequest;
import com.xin.ojcodesandbox.model.ExecuteCodeResponse;
import org.springframework.stereotype.Component;


/**
 * Python 原生代码沙箱实现（直接复用模板方法）
 *
 * @author 15712
 */
@Component
public class PythonNativeCodeSandbox extends PythonCodeSandBoxTemplate {

    /**
     * 执行代码
     *
     * @param executeCodeRequest
     * @return
     */
    @Override
    public ExecuteCodeResponse executeCode(ExecuteCodeRequest executeCodeRequest) {
        return super.executeCode(executeCodeRequest);
    }
}

