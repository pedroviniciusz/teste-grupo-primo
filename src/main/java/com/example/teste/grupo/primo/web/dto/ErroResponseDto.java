package com.example.teste.grupo.primo.web.dto;

import lombok.Builder;
import org.springframework.http.HttpStatus;

@Builder
public record ErroResponseDto(HttpStatus httpStatus, String mesagemErro) {
}
