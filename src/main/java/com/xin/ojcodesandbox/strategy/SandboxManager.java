package com.xin.ojcodesandbox.strategy;

import com.xin.ojcodesandbox.service.CodeSandBox;
import com.xin.ojcodesandbox.service.c.CNativeCodeSandbox;
import com.xin.ojcodesandbox.service.cpp.CppNativeCodeSandbox;
import com.xin.ojcodesandbox.service.java.JavaNativeCodeSandbox;
import com.xin.ojcodesandbox.service.python3.PythonNativeCodeSandbox;
import org.springframework.stereotype.Service;

/**
 * @author 15712
 * 判题管理，简化判题服务
 */
@Service
public class SandboxManager {


    public static CodeSandBox getSandBox(String language) {
        switch (language) {
            case "java":
                return new JavaNativeCodeSandbox();
            case "python":
                return new PythonNativeCodeSandbox();
            case "c":
                return new CNativeCodeSandbox();
            case "cpp":
                return new CppNativeCodeSandbox();
            default:
                throw new RuntimeException("不支持该语言");
        }

    }
}
