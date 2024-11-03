package com.example.teste.grupo.primo.core.repositorio;

import com.example.teste.grupo.primo.core.entidade.Conta;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ContaRepositorio extends JpaRepository<Conta, Integer> {

    /**
     * Busca uma conta pelo ID com bloqueio pessimista de escrita.
     * Utiliza `LockModeType.PESSIMISTIC_WRITE` para garantir que, em cenários
     * de alta concorrência, apenas uma transação por vez possa modificar a conta.
     * Esse bloqueio é importante para manter a consistência em operações
     * sensíveis, como saques e depósitos, evitando conflitos de acesso.
     *
     * @param id O id da conta
     * @return Um Optional com a conta encontrada, ou vazio se não existir
     */
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    Optional<Conta> findWithLockingById(Integer id);

}
