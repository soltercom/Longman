package ru.altercom.spb.longman.system;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping
public class RootController {

    public static final String REDIRECT_DICTIONARY = "redirect:/dictionary";

    @GetMapping
    public String index() {
        return REDIRECT_DICTIONARY;
    }

}
