package com.mycompany.taller.controlador;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class principalController {

    @GetMapping("/")
    public String inicio() {
        return "principal";
    }
}
