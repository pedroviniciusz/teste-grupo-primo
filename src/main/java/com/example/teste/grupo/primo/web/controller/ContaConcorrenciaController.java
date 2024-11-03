package com.example.teste.grupo.primo.web.controller;

import com.example.teste.grupo.primo.core.servico.ContaConcorrenciaServico;
import com.example.teste.grupo.primo.web.dto.concorrencia.DepositoSaqueDto;
import com.example.teste.grupo.primo.web.dto.concorrencia.DepositoTransferenciaDto;
import com.example.teste.grupo.primo.web.dto.concorrencia.TransferenciaTransferenciaDto;
import io.github.resilience4j.timelimiter.annotation.TimeLimiter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.function.FailableRunnable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@RestController
@RequestMapping("/api/conta-concorrencia")
@RequiredArgsConstructor
@Slf4j
public class ContaConcorrenciaController extends BaseRestController {

    private final ContaConcorrenciaServico contaConcorrenciaServico;

    @PostMapping("/deposito-saque")
    @TimeLimiter(name = "depositoSaqueTimeLimiter")
    public CompletableFuture<Void> depositoSaque(@RequestBody DepositoSaqueDto depositoSaqueDto) {
        log.info("Requisição para depósito e saque concorrentes");

        CompletableFuture<Void> depositoFuture = CompletableFuture.runAsync(() -> contaConcorrenciaServico.depositarConcorrencia(depositoSaqueDto.depositoDto()));
        CompletableFuture<Void> saqueFuture = CompletableFuture.runAsync(() -> contaConcorrenciaServico.sacarConcorrencia(depositoSaqueDto.saqueDto()));

        return CompletableFuture.allOf(depositoFuture, saqueFuture);
    }

    @PostMapping("/deposito-transferencia")
    @TimeLimiter(name = "depositoTransferenciaTimeLimiter")
    public CompletableFuture<Void> depositoTransferencia(@RequestBody DepositoTransferenciaDto depositoTransferenciaDto) {
        log.info("Requisição para depósito e saque concorrentes");

        CompletableFuture<Void> depositoFuture = CompletableFuture.runAsync(() -> contaConcorrenciaServico.depositarConcorrencia(depositoTransferenciaDto.depositoDto()));
        CompletableFuture<Void> transferenciaFuture = CompletableFuture.runAsync(() -> contaConcorrenciaServico.transferirConcorrencia(depositoTransferenciaDto.transferenciaDto()));

        return CompletableFuture.allOf(depositoFuture, transferenciaFuture);
    }

    @PostMapping("/transferencia-transferencia")
    @TimeLimiter(name = "transferenciaTransferenciaTimeLimiter")
    public CompletableFuture<Void> depositoTransferencia(@RequestBody TransferenciaTransferenciaDto transferenciaDto) {
        log.info("Requisição para transfência entre contas");

        CompletableFuture<Void> transferenciaFuture = CompletableFuture.runAsync(() -> contaConcorrenciaServico.transferirConcorrencia(transferenciaDto.transferenciaDto1()));
        CompletableFuture<Void> transferenciaFuture2 = CompletableFuture.runAsync(() -> contaConcorrenciaServico.transferirConcorrencia(transferenciaDto.transferenciaDto2()));

        return CompletableFuture.allOf(transferenciaFuture, transferenciaFuture2);
    }

}
