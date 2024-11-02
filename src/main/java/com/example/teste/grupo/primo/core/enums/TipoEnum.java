package com.example.teste.grupo.primo.core.enums;

import lombok.Getter;

@Getter
public enum TipoEnum {

    DEPOSITO("Depósito"),
    SAQUE("Saque"),
    TRANSFERENCIA("Transferência");

    private final String descricao;

    TipoEnum(String descricao) {
        this.descricao = descricao;
    }

}
