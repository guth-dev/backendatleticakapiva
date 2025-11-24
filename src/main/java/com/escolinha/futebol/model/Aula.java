package com.escolinha.futebol.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class Aula {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String data;       // Data completa da aula (ex: 2025-01-20)
    private String horaInicio;
    private String horaFim;
    private String titulo;

    @ManyToOne
    @JoinColumn(name = "turma_id")
    @JsonBackReference // Aula é "filha" da turma, evita loop
    private Turma turma;

    public Aula() {}
}
