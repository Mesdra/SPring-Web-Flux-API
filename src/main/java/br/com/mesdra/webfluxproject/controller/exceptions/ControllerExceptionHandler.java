package br.com.mesdra.webfluxproject.controller.exceptions;


import br.com.mesdra.webfluxproject.service.exception.ObjectNotFoundException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.support.WebExchangeBindException;
import reactor.core.publisher.Mono;

import static java.time.LocalDateTime.now;

@ControllerAdvice
public class ControllerExceptionHandler {

    @ExceptionHandler(DuplicateKeyException.class)
    ResponseEntity<Mono<StandardError>> duplicatedKeyException(DuplicateKeyException ex, ServerHttpRequest request) {
        return ResponseEntity.badRequest().body(
                Mono.just(
                        StandardError.builder()
                                     .timestamp(now())
                                     .status(HttpStatus.BAD_REQUEST.value())
                                     .error(HttpStatus.BAD_REQUEST.getReasonPhrase())
                                     .message(verifyDupKey(ex.getMessage()))
                                     .path(request.getPath().toString()).build()
                         ));
    }

    @ExceptionHandler(WebExchangeBindException.class)
    ResponseEntity<Mono<ValidationError>> validationError(WebExchangeBindException ex, ServerHttpRequest request) {
        ValidationError error =
                new ValidationError(now(), request.getPath().toString(), HttpStatus.BAD_REQUEST.value(), "Validation Error", "Erro de " +
                        "validação ed formulario");

        for (FieldError er : ex.getBindingResult().getFieldErrors()) {
            error.addError(er.getField(), er.getDefaultMessage());
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Mono.just(error));
    }

    @ExceptionHandler(ObjectNotFoundException.class)
    ResponseEntity<Mono<StandardError>> objectNotFound(ObjectNotFoundException ex, ServerHttpRequest request) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND.value()).body(
                Mono.just(
                        StandardError.builder()
                                     .timestamp(now())
                                     .status(HttpStatus.NOT_FOUND.value())
                                     .error(HttpStatus.NOT_FOUND.getReasonPhrase())
                                     .message(ex.getMessage())
                                     .path(request.getPath().toString()).build()
                         ));
    }

    private String verifyDupKey(String message) {
        if (message.contains("email dup key")) {
            return "Este E-mail já foi registrado";
        } else {
            return "Validação de chave duplicada";
        }
    }

}
