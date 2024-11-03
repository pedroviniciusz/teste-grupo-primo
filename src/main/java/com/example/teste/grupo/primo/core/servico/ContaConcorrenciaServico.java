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

import java.util.concurrent.locks.ReentrantLock;

@Service
@RequiredArgsConstructor
public class ContaConcorrenciaServico {

    private final ContaRepositorio contaRepositorio;
    private final TransacaoServico transacaoServico;

    private final ReentrantLock lock = new ReentrantLock();

    /**
     * Método responsável por buscar uma conta pelo ID com bloqueio pessimista de escrita.
     * Utiliza `LockModeType.PESSIMISTIC_WRITE` para garantir que, em cenários
     * de alta concorrência, apenas uma transação por vez possa modificar a conta
     *
     * @param id id da conta
     * @return Conta
     */
    public Conta findWithLockingById(Integer id) {
        return contaRepositorio.findWithLockingById(id).orElseThrow(() -> new EntityNotFoundException("Conta não encontrada"));
    }

    /**
     * Método responsável por depositar uma quantidade em uma conta e chamar o servico para salvar a transacao
     *
     * @param depositoDto depositoDto
     */
    @Transactional
    public void depositar(DepositoDto depositoDto) {
        Conta conta = findWithLockingById(depositoDto.idConta());
        conta.setSaldo(conta.getSaldo().add(depositoDto.valor()));

        contaRepositorio.save(conta);

        transacaoServico.registrarDeposito(conta, depositoDto.valor());
    }

    /**
     * Método responsável por sacar uma quantidade em uma conta e chamar o servico para salvar a transacao
     *
     * @param saqueDto saqueDto
     */
    @Transactional
    public void sacar(SaqueDto saqueDto) {
        Conta conta = findWithLockingById(saqueDto.idConta());

        ContaUtil.validarSaldo(conta.getSaldo(), saqueDto.valor());

        conta.setSaldo(conta.getSaldo().subtract(saqueDto.valor()));
        contaRepositorio.save(conta);

        transacaoServico.registrarSaque(conta, saqueDto.valor());
    }

    /**
     * Método responsável por transferir uma quantidade de uma conta para a outra e chamar o servico para salvar a transacao
     * Neste caso usamos o lock.lock() pois na concorrência 3  da tabela verdade efetuamos 2 tranferências ao mesmo tempo
     * Nos outros casos de concorrência. não é chamado o mesmo método, somente a mesma conta
     *
     * @param transferenciaDto transferenciaDto
     */
    @Transactional
    public synchronized void transferirConcorrencia(TransferenciaDto transferenciaDto) {
        lock.lock();
        try {
            Conta contaOrigem = findWithLockingById(transferenciaDto.idContaOrigem());

            ContaUtil.validarSaldo(contaOrigem.getSaldo(), transferenciaDto.valor());

            Conta contaDestino = findWithLockingById(transferenciaDto.idContaDestino());

            contaOrigem.setSaldo(contaOrigem.getSaldo().subtract(transferenciaDto.valor()));
            contaRepositorio.save(contaOrigem);

            contaDestino.setSaldo(contaDestino.getSaldo().add(transferenciaDto.valor()));
            contaRepositorio.save(contaDestino);

            transacaoServico.registrarTransferencia(contaOrigem, contaDestino, transferenciaDto.valor());
        } finally {
            lock.unlock();
        }
    }

    /**
     * Método responsável por chamar o método de depósito e pausa a thread por 1 segundo
     *
     * @param depositoDto depositoDto
     */
    @Transactional
    public void depositarConcorrencia(DepositoDto depositoDto) {
        try {
            depositar(depositoDto);
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Método responsável por chamar o método de saque e pausa a thread por 1 segundo
     *
     * @param saqueDto saqueDto
     */
    @Transactional
    public void sacarConcorrencia(SaqueDto saqueDto) {
        try {
            sacar(saqueDto);
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

    }
}
