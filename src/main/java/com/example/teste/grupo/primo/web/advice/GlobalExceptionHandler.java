package com.example.teste.grupo.primo.web.advice;

import com.example.teste.grupo.primo.core.execao.BadRequestException;
import com.example.teste.grupo.primo.core.execao.EntityNotFoundException;
import com.example.teste.grupo.primo.web.dto.ErroResponseDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<ErroResponseDto> handleBadRequestException(BadRequestException ex) {
        ErroResponseDto errorResponse = ErroResponseDto.builder()
                .httpStatus(HttpStatus.BAD_REQUEST)
                .mesagemErro(ex.getMessage())
                .build();
        log.error("GlobalExceptionHandler::handleBadRequestException execeção lançada {}", ex.getMessage());
        return ResponseEntity.badRequest().body(errorResponse);
    }

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<ErroResponseDto> handleBadRequestException(EntityNotFoundException ex) {
        ErroResponseDto errorResponse = ErroResponseDto.builder()
                .httpStatus(HttpStatus.NOT_FOUND)
                .mesagemErro(ex.getMessage())
                .build();
        log.error("GlobalExceptionHandler::handleEntityNotFoundException execeção lançada {}", ex.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErroResponseDto> handleBadRequestException(Exception ex) {
        ErroResponseDto errorResponse = ErroResponseDto.builder()
                .httpStatus(HttpStatus.INTERNAL_SERVER_ERROR)
                .mesagemErro(ex.getMessage())
                .build();
        log.error("GlobalExceptionHandler::handleEntityNotFoundException execeção lançada {}", ex.getMessage());
        return ResponseEntity.internalServerError().body(errorResponse);
    }

}
