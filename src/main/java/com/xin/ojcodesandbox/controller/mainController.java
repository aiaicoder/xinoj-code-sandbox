package com.xin.ojcodesandbox.controller;

import com.xin.ojcodesandbox.model.ExecuteCodeRequest;
import com.xin.ojcodesandbox.model.ExecuteCodeResponse;
import com.xin.ojcodesandbox.service.CodeSandBox;
import com.xin.ojcodesandbox.strategy.SandboxManager;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import static com.xin.ojcodesandbox.constant.AuthConstant.AUTHREQUESTHEADER;
import static com.xin.ojcodesandbox.constant.AuthConstant.AUTHREQUESTSECRET;

@RestController("/")
public class mainController {

    @GetMapping("/health")
    public String health() {
        return "ok";
    }

    @PostMapping("/executeCode")
    public ExecuteCodeResponse executeCode(@RequestBody ExecuteCodeRequest executeCodeRequest, HttpServletRequest request,
                                           HttpServletResponse response) {
        String authHeaders = request.getHeader(AUTHREQUESTHEADER);
        //基本认证
        if (!AUTHREQUESTSECRET.equals(authHeaders)) {
            response.setStatus(403);
            return null;
        }
        if (executeCodeRequest == null) {
            throw new RuntimeException("参数为空");
        }
        String language = executeCodeRequest.getLanguage();
        CodeSandBox sandBox = SandboxManager.getSandBox(language);
        return sandBox.executeCode(executeCodeRequest);
    }
}
