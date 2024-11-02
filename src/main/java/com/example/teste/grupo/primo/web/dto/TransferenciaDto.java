package com.example.teste.grupo.primo.web.dto;

import java.math.BigDecimal;

public record TransferenciaDto(Integer idContaOrigem, Integer idContaDestino, BigDecimal valor) {

}
