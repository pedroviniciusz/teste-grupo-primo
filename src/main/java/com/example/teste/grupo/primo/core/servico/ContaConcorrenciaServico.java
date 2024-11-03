package com.example.teste.grupo.primo.core.servico;

import com.example.teste.grupo.primo.core.entidade.Conta;
import com.example.teste.grupo.primo.core.execao.BadRequestException;
import com.example.teste.grupo.primo.core.execao.EntityNotFoundException;
import com.example.teste.grupo.primo.core.repositorio.ContaRepositorio;
import com.example.teste.grupo.primo.web.dto.DepositoDto;
import com.example.teste.grupo.primo.web.dto.SaqueDto;
import com.example.teste.grupo.primo.web.dto.TransferenciaDto;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.concurrent.locks.ReentrantLock;

@Service
@RequiredArgsConstructor
public class ContaConcorrenciaServico {

    private final ContaRepositorio contaRepositorio;
    private final TransacaoServico transacaoServico;

    private final ReentrantLock lock = new ReentrantLock();

    public Conta findContaById(Integer id) {
        return contaRepositorio.findWithLockingById(id).orElseThrow(() -> new EntityNotFoundException("Conta não encontrada"));
    }

    @Transactional
    public void depositar(DepositoDto depositoDto) {
        Conta conta = findContaById(depositoDto.idConta());
        conta.setSaldo(conta.getSaldo().add(depositoDto.valor()));

        contaRepositorio.save(conta);

        transacaoServico.registrarDeposito(conta, depositoDto.valor());
    }

    @Transactional
    public void sacar(SaqueDto saqueDto) {
        Conta conta = findContaById(saqueDto.idConta());

        validarSaldo(conta.getSaldo(), saqueDto.valor());

        conta.setSaldo(conta.getSaldo().subtract(saqueDto.valor()));
        contaRepositorio.save(conta);

        transacaoServico.registrarSaque(conta, saqueDto.valor());
    }

    @Transactional
    public void transferirConcorrencia(TransferenciaDto transferenciaDto) {
        lock.lock();
        try {
            Conta contaOrigem = findContaById(transferenciaDto.idContaOrigem());

            validarSaldo(contaOrigem.getSaldo(), transferenciaDto.valor());

            Conta contaDestino = findContaById(transferenciaDto.idContaDestino());

            contaOrigem.setSaldo(contaOrigem.getSaldo().subtract(transferenciaDto.valor()));
            contaRepositorio.save(contaOrigem);

            contaDestino.setSaldo(contaDestino.getSaldo().add(transferenciaDto.valor()));
            contaRepositorio.save(contaDestino);

            transacaoServico.registrarTransferencia(contaOrigem, contaDestino, transferenciaDto.valor());
        } finally {
            lock.unlock();
        }
    }

    @Transactional
    public void depositarConcorrencia(DepositoDto depositoDto) {
        try {
            depositar(depositoDto);
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    @Transactional
    public void sacarConcorrencia(SaqueDto saqueDto) {
        try {
            sacar(saqueDto);
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

    }

    private void validarSaldo(BigDecimal saldo, BigDecimal valorSacar) {
        if (saldo.compareTo(valorSacar) == -1) {
            throw new BadRequestException("Saldo insuficiente");
        }
    }
}
