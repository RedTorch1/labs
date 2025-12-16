package ru.ssau.tk.maths.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class TestController {

    @GetMapping("/test")
    public String test() {
        System.out.println("TEST CONTROLLER CALLED!");
        return "redirect:/test.html";  // ✅ HTML вместо JSP
    }
}
