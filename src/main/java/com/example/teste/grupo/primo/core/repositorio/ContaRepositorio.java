package com.example.teste.grupo.primo.core.repositorio;

import com.example.teste.grupo.primo.core.entidade.Conta;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ContaRepositorio extends JpaRepository<Conta, Integer> {

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    Optional<Conta>findWithLockingById(Integer id);

}
