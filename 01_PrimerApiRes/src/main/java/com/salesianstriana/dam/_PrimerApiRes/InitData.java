package com.salesianstriana.dam._PrimerApiRes;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.Arrays;
//Esta clase tiene como función añadir algunos datos de inicio a la base de datos mientras funcione sin persistencia.
@Component
@RequiredArgsConstructor
public class InitData {
    private final TaskRepository repository;

    @PostConstruct
    public void init (){
        repository.saveAll(
                Arrays.asList(
                        new Task("Rellenar initdata","Llena de datos el repositorio"),
                        new Task("Cerrar el proyecto","Antes de lanzar uno nuevo")
                )
        );

    }
}
