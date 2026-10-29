package com.salesianstriana.dam._PrimerApiRes;

import java.util.Map;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@RestControllerAdvice(assignableTypes = TaskController.class)
public class TaskErrors extends ResponseEntityExceptionHandler {
  @ExceptionHandler(ResponseStatusException.class)
  public ResponseEntity<Object> status(ResponseStatusException exception) {
    return ResponseEntity.status(exception.getStatus()).body(body(exception.getStatus()));
  }

  @Override
  protected ResponseEntity<Object> handleExceptionInternal(
      Exception exception,
      Object ignored,
      HttpHeaders headers,
      HttpStatus status,
      WebRequest request) {
    return new ResponseEntity<>(body(status), headers, status);
  }

  private Map<String, String> body(HttpStatus status) {
    String code =
        status == HttpStatus.NOT_FOUND
            ? "not_found"
            : status == HttpStatus.BAD_REQUEST ? "invalid_request" : "request_error";
    return Map.of("code", code);
  }
}
