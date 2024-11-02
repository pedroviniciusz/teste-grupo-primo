package com.example.teste.grupo.primo.web.dto;

import com.example.teste.grupo.primo.core.entidade.Conta;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@NoArgsConstructor
@Getter
@Setter
public class TransacaoDto {

    private String tipo;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Long conta;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Long origem;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Long destino;

    private BigDecimal valor;

    public TransacaoDto(String tipo, Conta conta, BigDecimal valor) {
        this.tipo = tipo;
        this.conta = conta.getNumero();
        this.valor = valor;
    }

    public TransacaoDto(String tipo, Conta origem, Conta destino, BigDecimal valor) {
        this.tipo = tipo;
        this.origem = origem.getNumero();
        this.destino = destino.getNumero();
        this.valor = valor;
    }
}
