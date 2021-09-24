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

    @GetMapping("/{id}")
    public Task findOne(@PathVariable ("id")Long id){
        return respository.findById(id).orElse(null);
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteOne(@PathVariable ("id") Long id){

         respository.deleteById(id);
         return ResponseEntity.noContent().build();
        // return ResponseEntity.status(204).build();
    }


}
