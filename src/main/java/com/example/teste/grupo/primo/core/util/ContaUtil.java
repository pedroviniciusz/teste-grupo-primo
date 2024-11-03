package com.example.teste.grupo.primo.core.util;

import com.example.teste.grupo.primo.core.execao.BadRequestException;

import java.math.BigDecimal;
import java.util.Random;

public class ContaUtil {

    private ContaUtil() {
    }

    /**
     * Método responsável por gerar um número aleatorio para aconta
     *
     * @return long
     */
    public static long gerarNumeroConta() {
        Random random = new Random();
        return Math.abs(random.nextLong() % 1_000_000_0000L);
    }

    /**
     * Método responsável validar o saldo
     *
     * @return long
     */
    public static void validarSaldo(BigDecimal saldo, BigDecimal valorSacar) {
        if (saldo.compareTo(valorSacar) == -1) {
            throw new BadRequestException("Saldo insuficiente");
        }
    }

}
