package com.example.teste.grupo.primo.core.execao;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.io.Serial;

@ResponseStatus(value = HttpStatus.BAD_REQUEST)
public class BadRequestException extends RuntimeException {

    @Serial
    private static final long serialVersionUID = -8093467163634326844L;

    public BadRequestException(String msg) {
        super(msg);
    }
}
