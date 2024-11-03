package com.example.teste.grupo.primo.core.servico;

import com.example.teste.grupo.primo.core.entidade.Conta;
import com.example.teste.grupo.primo.core.execao.EntityNotFoundException;
import com.example.teste.grupo.primo.core.repositorio.ContaRepositorio;
import com.example.teste.grupo.primo.core.util.ContaUtil;
import com.example.teste.grupo.primo.web.dto.DepositoDto;
import com.example.teste.grupo.primo.web.dto.SaqueDto;
import com.example.teste.grupo.primo.web.dto.TransferenciaDto;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
public class ContaServico {

    private final ContaRepositorio contaRepositorio;
    private final TransacaoServico transacaoServico;

    /**
     * Método responsável por buscar uma conta pelo ID, neste caso sem bloqueio pessimista
     *
     * @param id id da conta
     * @return Conta
     */
    public Conta findContaById(Integer id) {
        return contaRepositorio.findById(id).orElseThrow(() -> new EntityNotFoundException("Conta não encontrada"));
    }

    /**
     * Método responsável por depositar uma quantidade em uma conta e chamar o servico para salvar a transacao
     *
     * @param depositoDto depositoDto
     * @return Conta
     */
    @Transactional
    public Conta depositar(DepositoDto depositoDto) {
        Conta conta = findContaById(depositoDto.idConta());
        conta.setSaldo(conta.getSaldo().add(depositoDto.valor()));

        contaRepositorio.save(conta);

        transacaoServico.registrarDeposito(conta, depositoDto.valor());

        return conta;
    }

    /**
     * Método responsável por sacar uma quantidade em uma conta e chamar o servico para salvar a transacao
     *
     * @param saqueDto saqueDto
     * @return Conta
     */
    @Transactional
    public Conta sacar(SaqueDto saqueDto) {
        Conta conta = findContaById(saqueDto.idConta());

        ContaUtil.validarSaldo(conta.getSaldo(), saqueDto.valor());

        conta.setSaldo(conta.getSaldo().subtract(saqueDto.valor()));
        contaRepositorio.save(conta);

        transacaoServico.registrarSaque(conta, saqueDto.valor());

        return conta;
    }

    /**
     * Método responsável por transferir uma quantidade de uma conta para a outra e chamar o servico para salvar a transacao
     *
     * @param transferenciaDto transferenciaTransferenciaDto
     * @return Conta conta
     */
    @Transactional
    public Conta transferir(TransferenciaDto transferenciaDto) {
        Conta contaOrigem = findContaById(transferenciaDto.idContaOrigem());

        ContaUtil.validarSaldo(contaOrigem.getSaldo(), transferenciaDto.valor());

        Conta contaDestino = findContaById(transferenciaDto.idContaDestino());

        contaOrigem.setSaldo(contaOrigem.getSaldo().subtract(transferenciaDto.valor()));
        contaRepositorio.save(contaOrigem);

        contaDestino.setSaldo(contaDestino.getSaldo().add(transferenciaDto.valor()));
        contaRepositorio.save(contaDestino);

        transacaoServico.registrarTransferencia(contaOrigem, contaDestino, transferenciaDto.valor());

        return contaOrigem;
    }

    /**
     * Método responsável por criar uma conta
     *
     * @return Conta
     */
    @Transactional
    public Conta criarConta(Long valorInicial) {
        Conta conta = new Conta();
        conta.setNumero(ContaUtil.gerarNumeroConta());
        conta.setSaldo(BigDecimal.valueOf(valorInicial));

        return contaRepositorio.save(conta);
    }

}
