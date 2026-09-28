package com.aula.pos.appinscricao.model;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "tb_eventos")
public class Evento {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(length = 150, nullable = false)
    public String nome;

    @Column(nullable = false)
    public LocalDateTime dataHoraEvento;

    @Column(length = 200, nullable = false)
    public String local;

    @Column(length = 100, nullable = false)
    public String cidade;

    @Column(length = 2, nullable = false)
    public String estado;

    @Column(nullable = false)
    public Integer qtde;

}
