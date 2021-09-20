package com.ejemplo1.prueba;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class Controlador {

    @GetMapping({"/", "/index"})
    public String welcome() {
        return "index";


    }

}
