package com.example.teste.grupo.primo.web.dto.concorrencia;

import com.example.teste.grupo.primo.web.dto.DepositoDto;
import com.example.teste.grupo.primo.web.dto.TransferenciaDto;

public record DepositoTransferenciaDto(DepositoDto depositoDto, TransferenciaDto transferenciaDto) {
}
