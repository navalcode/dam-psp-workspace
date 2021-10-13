package com.salesianostriana.dam._PrimerProyectoSpringData.model;

import lombok.*;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
@NoArgsConstructor
//@Getter  @Setter @EqualsAndHashCode
@Data
public class Alumno {

    @Id @GeneratedValue
    private Long id;

    private String nombre, apellido, email;


}
