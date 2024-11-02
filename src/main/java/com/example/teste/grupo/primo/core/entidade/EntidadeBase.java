package com.example.teste.grupo.primo.core.entidade;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import lombok.Getter;
import lombok.Setter;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

@MappedSuperclass
@Getter
@Setter
public abstract class EntidadeBase implements Serializable {

    @Serial
    private static final long serialVersionUID = 1021449034388948230L;

    public abstract Integer getId();

    @JsonIgnore
    @Column(name = "CRIADO_EM", updatable = false)
    private LocalDateTime criado;

    @JsonIgnore
    @Column(name = "ATUALIZADO_EM")
    private LocalDateTime atualizado;

    @JsonIgnore
    private Boolean excluido;

    @PrePersist
    private void setCriado() {
        setCriado(LocalDateTime.now());
        setExcluido(false);
    }

    @PreUpdate
    private void setAtualizado() {
        setAtualizado(LocalDateTime.now());
    }

}
