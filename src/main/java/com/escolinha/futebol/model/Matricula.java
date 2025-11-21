package com.escolinha.futebol.model;

import com.escolinha.futebol.model.enums.StatusMatricula;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
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

    private LocalDate dataFim; // Data de término (se houver)

    @Enumerated(EnumType.STRING) // Salva o nome do Enum (ATIVA, INATIVA) no BD
    @Column(nullable = false)
    private StatusMatricula status = StatusMatricula.ATIVA; // Padrão

    // --- Relacionamento N:1 ---
    // Muitas matrículas para um aluno
    @NotNull
    @ManyToOne
    @JoinColumn(name = "aluno_id", nullable = false)
    private Aluno aluno;

    // --- Relacionamento N:1 ---
    // Muitas matrículas para uma turma
    @NotNull
    @ManyToOne
    @JoinColumn(name = "turma_id", nullable = false)
    private Turma turma;
}