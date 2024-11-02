package com.example.teste.grupo.primo.web.controller;

import com.example.teste.grupo.primo.core.repositorio.TransacaoRepositorio;
import com.example.teste.grupo.primo.core.servico.TransacaoServico;
import com.example.teste.grupo.primo.web.dto.DepositoDto;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/transacao")
@RequiredArgsConstructor
public class TransacaoController extends BaseRestController {

    private final TransacaoServico transacaoServico;

}
