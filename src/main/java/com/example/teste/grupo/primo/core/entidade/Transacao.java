package com.example.teste.grupo.primo.core.entidade;

import com.example.teste.grupo.primo.core.enums.TipoEnum;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Entity
@Table(name = "transacao")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Transacao extends EntidadeBase {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonIgnore
    private Integer id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TipoEnum tipo;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "CONTA_ID")
    private Conta conta;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "ORIGEM_ID", referencedColumnName = "id")
    private Conta origem;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "DESTINO_ID", referencedColumnName = "id")
    private Conta destino;

    @Column(nullable = false)
    private BigDecimal valor;

    @Version
    private Long version;

}
