package com.example.teste.grupo.primo.web.controller;

import com.example.teste.grupo.primo.core.servico.ContaServico;
import com.example.teste.grupo.primo.web.dto.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/conta")
@RequiredArgsConstructor
@Slf4j
public class ContaController extends BaseRestController {

    private final ContaServico contaServico;

    @GetMapping("/{id}")
    public ResponseEntity<ContaDto> getContaById(@PathVariable Integer id) {
        log.info("Requisição para obter a conta pelo id: {}", id);
        return writeResponseBody(ContaDto.transferToDto(contaServico.findContaById(id)));
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<ContaDto> criarConta() {
        log.info("Requisição para criar conta");
        return writeResponseBody(ContaDto.transferToDto(contaServico.criarConta()));
    }

    @PutMapping("/deposito")
    public ResponseEntity<ContaDto> depositar(@RequestBody DepositoDto depositoDto) {
        log.info("Requisição para depositar: {} na conta: {}", depositoDto.valor(), depositoDto.idConta());
        return writeResponseBody(ContaDto.transferToDto(contaServico.depositar(depositoDto)));
    }

    @PutMapping("/saque")
    public ResponseEntity<ContaDto> depositar(@RequestBody SaqueDto saqueDto) {
        log.info("Requisição para sacar: {} na conta: {}", saqueDto.valor(), saqueDto.idConta());
        return writeResponseBody(ContaDto.transferToDto(contaServico.sacar(saqueDto)));
    }

    @PutMapping("/transferencia")
    public ResponseEntity<ContaDto> depositar(@RequestBody TransferenciaDto transferenciaTransferenciaDto) {
        log.info("Requisição para transferir da conta: {} para conta: {} no valor de: {}", transferenciaTransferenciaDto.idContaOrigem(), transferenciaTransferenciaDto.idContaDestino(), transferenciaTransferenciaDto.valor());
        return writeResponseBody(ContaDto.transferToDto(contaServico.transferir(transferenciaTransferenciaDto)));
    }


}
