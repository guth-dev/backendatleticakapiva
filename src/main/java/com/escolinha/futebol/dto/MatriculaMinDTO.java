package com.escolinha.futebol.dto;

import com.escolinha.futebol.model.Matricula;

public record MatriculaMinDTO(
        Long id,
        String dataMatricula,
        String status,
        Long alunoId,
        String alunoNome
) {
    public static MatriculaMinDTO fromEntity(Matricula m) {
        return new MatriculaMinDTO(
                m.getId(),
                m.getDataMatricula().toString(),
                m.getStatus().name(),
                m.getAluno().getId(),
                m.getAluno().getNome()
        );
    }
}

