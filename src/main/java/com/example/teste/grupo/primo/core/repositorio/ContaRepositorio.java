package com.example.teste.grupo.primo.core.repositorio;

import com.example.teste.grupo.primo.core.entidade.Conta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ContaRepositorio extends JpaRepository<Conta, Integer> {
}
