package com.escolinha.futebol.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

import java.time.LocalTime;
import java.util.List; // Importar List

@Entity
@Data
@Table(name = "turmas")
public class Turma {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "O nome da turma é obrigatório.")
    @Column(nullable = false)
    private String nome; // Ex: "Sub-10 Manhã"

    @Positive(message = "A faixa etária mínima deve ser positiva.")
    private Integer faixaEtariaMinima;

    @Positive(message = "A faixa etária máxima deve ser positiva.")
    private Integer faixaEtariaMaxima;

    private String diaSemana; // Ex: "Segunda/Quarta", "Terça/Quinta"
    private LocalTime horarioInicio;
    private LocalTime horarioFim;

    @Positive(message = "O limite de alunos deve ser positivo.")
    private Integer limiteAlunos;

    // --- Relacionamento N:1 ---
    // Muitas turmas podem pertencer a um professor
    @NotNull(message = "A turma deve ter um professor.")
    @ManyToOne
    @JoinColumn(name = "professor_id", nullable = false) // Define a coluna de chave estrangeira
    private Professor professor;

    // --- Relacionamento 1:N ---
    // Uma turma pode ter várias matrículas
    @OneToMany(mappedBy = "turma", cascade = CascadeType.ALL)
    private List<Matricula> matriculas;
}