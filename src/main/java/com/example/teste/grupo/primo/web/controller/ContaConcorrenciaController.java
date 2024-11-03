package com.example.teste.grupo.primo.web.controller;

import com.example.teste.grupo.primo.core.servico.ContaConcorrenciaServico;
import com.example.teste.grupo.primo.web.dto.concorrencia.DepositoSaqueDto;
import com.example.teste.grupo.primo.web.dto.concorrencia.DepositoTransferenciaDto;
import com.example.teste.grupo.primo.web.dto.concorrencia.TransferenciaTransferenciaDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.function.FailableRunnable;
import org.springframework.web.bind.annotation.*;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@RestController
@RequestMapping("/api/conta-concorrencia")
@RequiredArgsConstructor
@Slf4j
public class ContaConcorrenciaController extends BaseRestController {

    private final ContaConcorrenciaServico contaConcorrenciaServico;

    @PostMapping("/deposito-saque")
    public void depositoSaque(@RequestBody DepositoSaqueDto depositoSaqueDto) {
        log.info("Requisição para depósito e saque concorrentes");
        ExecutorService executor = Executors.newFixedThreadPool(2);
        executor.execute(run(() ->  contaConcorrenciaServico.depositarConcorrencia(depositoSaqueDto.depositoDto())));
        executor.execute(run(() -> contaConcorrenciaServico.sacarConcorrencia(depositoSaqueDto.saqueDto())));
        executor.shutdown();
    }

    @PostMapping("/deposito-transferencia")
    public void depositoTransferencia(@RequestBody DepositoTransferenciaDto depositoTransferenciaDto) {
        log.info("Requisição para depósito e saque concorrentes");
        ExecutorService executor = Executors.newFixedThreadPool(2);
        executor.execute(run(() ->  contaConcorrenciaServico.depositarConcorrencia(depositoTransferenciaDto.depositoDto())));
        executor.execute(run(() -> contaConcorrenciaServico.transferirConcorrencia(depositoTransferenciaDto.transferenciaDto())));
        executor.shutdown();
    }

    @PostMapping("/transferencia-transferencia")
    public void depositoTransferencia(@RequestBody TransferenciaTransferenciaDto transferenciaDto) {
        log.info("Requisição para transfência entre contas");
        ExecutorService executor = Executors.newFixedThreadPool(2);
        executor.execute(run(() ->  contaConcorrenciaServico.transferirConcorrencia(transferenciaDto.transferenciaDto1())));
        executor.execute(run(() -> contaConcorrenciaServico.transferirConcorrencia(transferenciaDto.transferenciaDto2())));
        executor.shutdown();
    }

    private Runnable run(FailableRunnable<Exception> runnable) {
        return () -> {
            try {
                runnable.run();
            } catch (Exception e) {
                e.printStackTrace();
            }
        };
    }

}
