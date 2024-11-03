package com.example.teste.grupo.primo.web.controller;

import com.example.teste.grupo.primo.core.servico.ContaConcorrenciaServico;
import com.example.teste.grupo.primo.web.dto.concorrencia.DepositoSaqueDto;
import com.example.teste.grupo.primo.web.dto.concorrencia.DepositoTransferenciaDto;
import com.example.teste.grupo.primo.web.dto.concorrencia.TransferenciaTransferenciaDto;
import io.github.resilience4j.timelimiter.annotation.TimeLimiter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping("/api/conta-concorrencia")
@RequiredArgsConstructor
@Slf4j
public class ContaConcorrenciaController extends BaseRestController {

    private final ContaConcorrenciaServico contaConcorrenciaServico;

    /**
     * Método responsável por executar a concorrência 1 da tabela verdade
     * Executa o depósito e saque para a mesma conta em paralelo
     *
     * @param depositoSaqueDto Dto que contém o objeto para depósito e o objeto para saque
     * @return void
     */
    @PostMapping("/deposito-saque")
    @TimeLimiter(name = "depositoSaqueTimeLimiter")
    public CompletableFuture<Void> depositoSaque(@RequestBody DepositoSaqueDto depositoSaqueDto) {
        log.info("Requisição para depósito na conta: {} e saque na conta: {} concorrentes", depositoSaqueDto.depositoDto().idConta(), depositoSaqueDto.saqueDto().idConta());

        CompletableFuture<Void> depositoFuture = CompletableFuture.runAsync(() -> contaConcorrenciaServico.depositarConcorrencia(depositoSaqueDto.depositoDto()));
        CompletableFuture<Void> saqueFuture = CompletableFuture.runAsync(() -> contaConcorrenciaServico.sacarConcorrencia(depositoSaqueDto.saqueDto()));

        return CompletableFuture.allOf(depositoFuture, saqueFuture);
    }

    /**
     * Método responsável por executar a concorrência 2 da tabela verdade
     * Executa o depósito e transferência para oura conta em paralelo
     *
     * @param depositoTransferenciaDto Dto que contém o objeto para depósito e o objeto para transferência
     * @return void
     */
    @PostMapping("/deposito-transferencia")
    @TimeLimiter(name = "depositoTransferenciaTimeLimiter")
    public CompletableFuture<Void> depositoTransferencia(@RequestBody DepositoTransferenciaDto depositoTransferenciaDto) {
        log.info("Requisição para depósito na conta: {} e transferencia da conta: {} para conta {} concorrentes",
                depositoTransferenciaDto.depositoDto().idConta(),
                depositoTransferenciaDto.transferenciaDto().idContaOrigem(),
                depositoTransferenciaDto.transferenciaDto().idContaDestino());

        CompletableFuture<Void> depositoFuture = CompletableFuture.runAsync(() -> contaConcorrenciaServico.depositarConcorrencia(depositoTransferenciaDto.depositoDto()));
        CompletableFuture<Void> transferenciaFuture = CompletableFuture.runAsync(() -> contaConcorrenciaServico.transferirConcorrencia(depositoTransferenciaDto.transferenciaDto()));

        return CompletableFuture.allOf(depositoFuture, transferenciaFuture);
    }

    /**
     * Método responsável por executar a concorrência 3 da tabela verdade
     * Executa transferências entre 3 contas em paralelo
     *
     * @param transferenciaDto Dto que contém o objeto para as transferências
     * @return void
     */
    @PostMapping("/transferencia-transferencia")
    @TimeLimiter(name = "transferenciaTransferenciaTimeLimiter")
    public CompletableFuture<Void> depositoTransferencia(@RequestBody TransferenciaTransferenciaDto transferenciaDto) {
        log.info("Requisição para transfência da conta: {} para a conta: {} no valor de: {} e logo depois, da conta: {} para conta: {} no valor de: {}",
                transferenciaDto.transferenciaDto1().idContaOrigem(),
                transferenciaDto.transferenciaDto1().idContaDestino(),
                transferenciaDto.transferenciaDto1().valor(),
                transferenciaDto.transferenciaDto2().idContaOrigem(),
                transferenciaDto.transferenciaDto2().idContaDestino(),
                transferenciaDto.transferenciaDto2().valor());

        CompletableFuture<Void> transferenciaFuture = CompletableFuture.runAsync(() -> contaConcorrenciaServico.transferirConcorrencia(transferenciaDto.transferenciaDto1()));
        CompletableFuture<Void> transferenciaFuture2 = CompletableFuture.runAsync(() -> contaConcorrenciaServico.transferirConcorrencia(transferenciaDto.transferenciaDto2()));

        return CompletableFuture.allOf(transferenciaFuture, transferenciaFuture2);
    }

}
