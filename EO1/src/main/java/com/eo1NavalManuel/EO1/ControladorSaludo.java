package com.eo1NavalManuel.EO1;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.atomic.AtomicLong;

@RestController
public class ControladorSaludo {

    private static final String template = "Hola, %s!";
    private final AtomicLong counter = new AtomicLong();

    @GetMapping("/saludo")
    public Saludo saludo(@RequestParam(value = "name", defaultValue = "mundo") String name) {

        return new Saludo(counter.getAndIncrement(), String.format(template, name));
    }
}
