package com.example.teste.grupo.primo.core.servico;

import com.example.teste.grupo.primo.core.entidade.Conta;
import com.example.teste.grupo.primo.core.entidade.Transacao;
import com.example.teste.grupo.primo.core.enums.TipoEnum;
import com.example.teste.grupo.primo.core.repositorio.TransacaoRepositorio;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
public class TransacaoServico {

    private final TransacaoRepositorio transacaoRepositorio;

    public void registrarDeposito(Conta conta, BigDecimal valor) {
        Transacao transacao = new Transacao();
        transacao.setTipo(TipoEnum.DEPOSITO);
        transacao.setConta(conta);
        transacao.setValor(valor);

        transacaoRepositorio.save(transacao);
    }

    public void registrarSque(Conta conta, BigDecimal valor) {
        Transacao transacao = new Transacao();
        transacao.setTipo(TipoEnum.SAQUE);
        transacao.setConta(conta);
        transacao.setValor(valor);

        transacaoRepositorio.save(transacao);
    }

    public void registrarTransferencia(Conta contaOrigem, Conta contaDestino, BigDecimal valor) {
        Transacao transacao = new Transacao();
        transacao.setTipo(TipoEnum.TRANSFERENCIA);
        transacao.setOrigem(contaOrigem);
        transacao.setDestino(contaDestino);
        transacao.setValor(valor);

        transacaoRepositorio.save(transacao);
    }

}
