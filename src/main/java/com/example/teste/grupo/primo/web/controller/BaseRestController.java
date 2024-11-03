package com.example.teste.grupo.primo.web.controller;

import org.springframework.http.ResponseEntity;

import java.util.List;

import static com.example.teste.grupo.primo.core.util.NullUtil.isNotNull;

public abstract class BaseRestController {

    protected <T> ResponseEntity<T> writeResponseBody(T body) {
        return ResponseEntity.ok(body);
    }

}
