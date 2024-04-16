package com.xin.ojcodesandbox;

import com.xin.ojcodesandbox.model.ExecuteCodeRequest;
import com.xin.ojcodesandbox.model.ExecuteCodeResponse;

/**
 * @author 15712
 */
public interface CodeSandBox {

    ExecuteCodeResponse executeCode(ExecuteCodeRequest executeCodeRequest);
}
