package com.salesianstriana.dam._PrimerApiRes;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/task")
@RequiredArgsConstructor

public class TaskController {

    private final TaskRepository respository;

    @GetMapping("/")
    public List<Task> findAll() {
        return respository.findAll();
    }

    @PostMapping("/")
    public ResponseEntity<Task> create(@RequestBody Task task) {
        //return respository.save(task); //De esta forma funciona, pero no mandamos el código correcto http.

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(respository.save(task)); //Con este código mandamos el código de respuesta correcto, guardamos en
        //el repositorio los datos y además devolvemos la información.
    }

    //Este método nos permite encontrar una entidad por su id.
    @GetMapping("/{id}")
    public Task findOne(@PathVariable("id") Long id) {
        //devuelve un elemento, task, por su id. En caso de no encontrarlo devuelve null.
        // Además devuelve el código por defecto 200, ok.
        return respository.findById(id).orElse(null);
    }

    //Este método nos permite eliminar una entidad por su id.
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteOne(@PathVariable("id") Long id) {

        //Primero borramos del repositorio el elemento dado su id.
        respository.deleteById(id);
        //Posteriormente lanzamos un mensaje al cliente, en este caso debemos mandar un código 204.
        //podemos hacerlos de las dos maneras siguientes.
        return ResponseEntity.noContent().build();
        // return ResponseEntity.status(204).build();
    }
    @PutMapping("/{id}")
    public Task edit(@RequestBody Task task, @PathVariable Long id){

        Task antigua = respository.findById(id).orElse(null);
        antigua.setText(task.getText());
        antigua.setTitle(task.getTitle());

        return respository.save(antigua);

    }


}
