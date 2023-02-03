package me.nerminsehic.groupevent.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/")
@RestController
public class HelloController {

    @GetMapping
    public String getMessage() {
        return "Groupevent API v1";
    }
}
