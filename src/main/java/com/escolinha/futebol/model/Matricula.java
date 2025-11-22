package com.escolinha.futebol.model;

import com.escolinha.futebol.model.enums.StatusMatricula;
import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDate;

@Entity
@Data
@Table(name = "matriculas")
public class Matricula {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private LocalDate dataMatricula = LocalDate.now();

    private LocalDate dataFim;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private StatusMatricula status = StatusMatricula.ATIVA;

    @ManyToOne
    @JoinColumn(name = "aluno_id", nullable = false)
    private Aluno aluno;

    // Agora a turma pode ser nula, para matrículas iniciais sem turma
    @ManyToOne
    @JoinColumn(name = "turma_id")
    private Turma turma;
}
