package com.escolinha.futebol.dto;

import com.escolinha.futebol.model.Turma;

public record TurmaMinDTO(
        Long id,
        String nome
) {
    public static TurmaMinDTO fromEntity(Turma turma) {
        if (turma == null) return null;
        return new TurmaMinDTO(turma.getId(), turma.getNome());
    }
}