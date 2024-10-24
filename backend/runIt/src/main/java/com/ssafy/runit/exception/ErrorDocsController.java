package com.ssafy.runit.exception;

import com.ssafy.runit.exception.code.AuthErrorCode;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.*;

@Controller
@RequestMapping("/docs")
public class ErrorDocsController {

    @GetMapping("/error")
    public String getAllErrorCodes(Model model) {
        Map<String, List<ErrorCodeType>> errorCodesMap = new HashMap<>();
        errorCodesMap.put("Auth", new ArrayList<>(Arrays.asList(AuthErrorCode.values())));
        model.addAttribute("errorCodesMap", errorCodesMap);
        return "error-docs";
    }
}
