package com.example.teste.grupo.primo.core.execao;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.io.Serial;

@ResponseStatus(value = HttpStatus.NOT_FOUND)
public class EntityNotFoundException extends RuntimeException {

    @Serial
    private static final long serialVersionUID = 224061857018543935L;

    public EntityNotFoundException(String msg) {
        super(msg);
    }
}
