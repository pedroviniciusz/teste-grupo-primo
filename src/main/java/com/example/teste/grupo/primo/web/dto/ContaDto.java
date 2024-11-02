package com.example.teste.grupo.primo.web.dto;

import com.example.teste.grupo.primo.core.entidade.Conta;
import com.example.teste.grupo.primo.core.entidade.Transacao;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class ContaDto {

    private Integer id;
    private Long numero;
    private BigDecimal saldo;
    private List<TransacaoDto> transacoes;

    public ContaDto(Conta conta) {
        this.id = conta.getId();
        this.numero = conta.getNumero();
        this.saldo = conta.getSaldo();
        this.transacoes = getTransacoes(conta.getTransacoes(), conta.getTransacoesOrigem(), conta.getTransacoesDestino());
    }

    public static ContaDto transferToDto(Conta conta) {
        return new ContaDto(conta);
    }

    private List<TransacaoDto> getTransacoes(List<Transacao> transacoes, List<Transacao> transacoesOrigem, List<Transacao> transacoesDestino) {
        List<TransacaoDto> transacaoDTOs = new ArrayList<>();

        transacoes.forEach(transacao -> transacaoDTOs.add(
                new TransacaoDto(transacao.getTipo().getDescricao(),
                        transacao.getConta(),
                        transacao.getValor())));

        transacoesOrigem.forEach(transacao -> transacaoDTOs.add(
                new TransacaoDto(transacao.getTipo().getDescricao(),
                        transacao.getOrigem(),
                        transacao.getDestino(),
                        transacao.getValor())));

        transacoesDestino.forEach(transacao -> transacaoDTOs.add(
                new TransacaoDto(transacao.getTipo().getDescricao(),
                        transacao.getOrigem(),
                        transacao.getDestino(),
                        transacao.getValor())));

        return transacaoDTOs;
    }

}
