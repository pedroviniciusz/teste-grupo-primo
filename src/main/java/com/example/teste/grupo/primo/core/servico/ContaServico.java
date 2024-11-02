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
import java.util.Random;

@Service
@RequiredArgsConstructor
public class ContaServico {

    private final ContaRepositorio contaRepositorio;
    private final TransacaoServico transacaoServico;

    public Conta findContaById(Integer id) {
        return contaRepositorio.findById(id).orElseThrow(() -> new EntityNotFoundException("Conta não encontrada"));
    }

    @Transactional
    public Conta depositar(DepositoDto depositoDto) {
        Conta conta = findContaById(depositoDto.idConta());
        conta.setSaldo(conta.getSaldo().add(depositoDto.valor()));

        contaRepositorio.save(conta);

        transacaoServico.registrarDeposito(conta, depositoDto.valor());

        return conta;
    }

    @Transactional
    public Conta sacar(SaqueDto saqueDto) {
        Conta conta = findContaById(saqueDto.idConta());

        validarSaldo(conta.getSaldo(), saqueDto.valor());

        conta.setSaldo(conta.getSaldo().subtract(saqueDto.valor()));
        contaRepositorio.save(conta);

        transacaoServico.registrarSque(conta, saqueDto.valor());

        return conta;
    }

    @Transactional
    public Conta transferir(TransferenciaDto transferenciaDto) {
        Conta contaOrigem = findContaById(transferenciaDto.idContaOrigem());

        validarSaldo(contaOrigem.getSaldo(), transferenciaDto.valor());

        Conta contaDestino = findContaById(transferenciaDto.idContaDestino());

        contaOrigem.setSaldo(contaOrigem.getSaldo().subtract(transferenciaDto.valor()));
        contaRepositorio.save(contaOrigem);

        contaDestino.setSaldo(contaDestino.getSaldo().add(transferenciaDto.valor()));
        contaRepositorio.save(contaDestino);

        transacaoServico.registrarTransferencia(contaOrigem, contaDestino, transferenciaDto.valor());

        return contaOrigem;
    }

    @Transactional
    public Conta criarConta() {
        Conta conta = new Conta();
        conta.setNumero(gerarNumeroConta());
        conta.setSaldo(BigDecimal.ZERO);

        return contaRepositorio.save(conta);
    }

    private static long gerarNumeroConta() {
        Random random = new Random();
        return Math.abs(random.nextLong() % 1_000_000_0000L);
    }

    private void validarSaldo(BigDecimal saldo, BigDecimal valorSacar) {
        if (saldo.compareTo(valorSacar) == -1) {
            throw new BadRequestException("Saldo insuficiente");
        }
    }
}
