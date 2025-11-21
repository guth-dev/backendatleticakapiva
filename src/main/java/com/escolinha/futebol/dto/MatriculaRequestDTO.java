package com.escolinha.futebol.dto;

import jakarta.validation.constraints.NotNull;

public record MatriculaRequestDTO(
        @NotNull(message = "O ID do aluno é obrigatório.")
        Long alunoId,

        @NotNull(message = "O ID da turma é obrigatório.")
        Long turmaId
) {
}