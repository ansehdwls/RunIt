package com.ssafy.runit.exception;

import com.ssafy.runit.exception.code.*;
import lombok.extern.slf4j.Slf4j;
import org.checkerframework.checker.units.qual.A;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.*;

@Controller
@RequestMapping("/error-docs")
@Slf4j
public class ErrorCodeController {

    @GetMapping("")
    public String getAllErrorCodes(Model model) {
        TreeMap<String, List<ErrorCodeType>> errorCodesMap = new TreeMap<>();
        errorCodesMap.put("Auth", new ArrayList<>(Arrays.asList(AuthErrorCode.values())));
        errorCodesMap.put("Group", new ArrayList<>(Arrays.asList(GroupErrorCode.values())));
        errorCodesMap.put("Server", new ArrayList<>(Arrays.asList(ServerErrorCode.values())));
        errorCodesMap.put("Experience", new ArrayList<>(Arrays.asList(ExperienceErrorCode.values())));
        errorCodesMap.put("League", new ArrayList<>(Arrays.asList(LeagueErrorCode.values())));
        errorCodesMap.put("Summary", new ArrayList<>(Arrays.asList(LeagueSummaryErrorCode.values())));
        model.addAttribute("errorCodesMap", errorCodesMap);
        return "error-docs";
    }
}