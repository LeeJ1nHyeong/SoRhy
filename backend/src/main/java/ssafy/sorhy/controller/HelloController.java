package ssafy.sorhy.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloController {

    @GetMapping("/health/check")
    public String hello() {
        return "ok";
    }
}
