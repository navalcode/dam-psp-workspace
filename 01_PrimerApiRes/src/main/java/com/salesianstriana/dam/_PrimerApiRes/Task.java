package com.salesianstriana.dam._PrimerApiRes;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

//Esta clase genera una entidad en la base de datos.
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Task {

    //Las anotaciones id y generatedvalue permiten que el id se gener automaticamente.
    @Id
    @GeneratedValue
    private long id;
    private String title;
    private String text;
//Dado que lombok crea constructores con todos los parámetros o vacios necesitamos un constructor sin el id, pero
    //que contenga otros parámetros, de esta forma  el id se puede autogenerar.
    public Task(String title, String text) {
        this.title = title;
        this.text = text;
    }
}
