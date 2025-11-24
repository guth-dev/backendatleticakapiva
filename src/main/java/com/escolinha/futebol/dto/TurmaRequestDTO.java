package com.escolinha.futebol.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import java.util.List;

public record TurmaRequestDTO(

        @NotBlank(message = "O nome da turma é obrigatório.")
        String nome,

        @Positive(message = "Faixa etária mínima deve ser positiva.")
        Integer faixaEtariaMinima,

        @Positive(message = "Faixa etária máxima deve ser positiva.")
        Integer faixaEtariaMaxima,

        @Positive(message = "Limite de alunos deve ser positivo.")
        Integer limiteAlunos,

        @NotNull(message = "O ID do professor é obrigatório.")
        Long professorId,

        List<Long> alunoIds
) {}
