package com.escolinha.futebol.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;
import java.util.List;

@Entity
@Data
@Table(name = "turmas")
public class Turma {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "O nome da turma é obrigatório.")
    @Column(nullable = false)
    private String nome;

    @Positive(message = "A faixa etária mínima deve ser positiva.")
    private Integer faixaEtariaMinima;

    @Positive(message = "A faixa etária máxima deve ser positiva.")
    private Integer faixaEtariaMaxima;

    @Positive(message = "O limite de alunos deve ser positivo.")
    private Integer limiteAlunos;

    @NotNull(message = "A turma deve ter um professor.")
    @ManyToOne
    @JoinColumn(name = "professor_id", nullable = false)
    private Professor professor;

    @OneToMany(mappedBy = "turma", cascade = CascadeType.ALL)
    @JsonManagedReference
    private List<Matricula> matriculas;
}
