package com.example.teste.grupo.primo.core.entidade;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "conta")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Conta extends EntidadeBase {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false, unique = true)
    private Long numero;

    @Column(nullable = false)
    private BigDecimal saldo;

    @OneToMany(mappedBy = "conta", cascade = CascadeType.ALL)
    private List<Transacao> transacoes = new ArrayList<>();

    @OneToMany(mappedBy = "origem", cascade = CascadeType.ALL)
    private List<Transacao> transacoesOrigem = new ArrayList<>();

    @OneToMany(mappedBy = "destino", cascade = CascadeType.ALL)
    private List<Transacao> transacoesDestino = new ArrayList<>();

}
