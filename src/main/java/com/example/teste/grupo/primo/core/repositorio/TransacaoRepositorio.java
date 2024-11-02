package com.example.teste.grupo.primo.core.repositorio;

import com.example.teste.grupo.primo.core.entidade.Transacao;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TransacaoRepositorio extends JpaRepository<Transacao, Integer> {
}
