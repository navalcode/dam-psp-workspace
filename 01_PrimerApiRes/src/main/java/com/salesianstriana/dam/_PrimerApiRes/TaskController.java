package com.salesianstriana.dam._PrimerApiRes;

import java.util.List;
import javax.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/task")
@RequiredArgsConstructor
@Transactional
public class TaskController {
  private final TaskRepository repository;

  @GetMapping("/")
  public List<Task> findAll() {
    return repository.findAll();
  }

  @PostMapping("/")
  public ResponseEntity<Task> create(@RequestBody Task task) {
    if (task.getId() != 0) throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
    validate(task);
    return ResponseEntity.status(HttpStatus.CREATED)
        .body(repository.save(new Task(task.getTitle(), task.getText())));
  }

  @GetMapping("/{id}")
  public Task findOne(@PathVariable("id") Long id) {
    return repository
        .findById(id)
        .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<Void> deleteOne(@PathVariable("id") Long id) {
    repository.delete(findOne(id));
    return ResponseEntity.noContent().build();
  }

  @PutMapping("/{id}")
  public Task edit(@RequestBody Task task, @PathVariable Long id) {
    Task existing = findOne(id);
    validate(task);
    existing.setTitle(task.getTitle());
    existing.setText(task.getText());
    return repository.save(existing);
  }

  private void validate(Task task) {
    if (task.getTitle() == null
        || task.getTitle().isBlank()
        || task.getTitle().length() > 255
        || (task.getText() != null && task.getText().length() > 255))
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
  }
}
