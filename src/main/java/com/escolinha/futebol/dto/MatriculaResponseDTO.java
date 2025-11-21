package com.escolinha.futebol.dto;

import com.escolinha.futebol.model.Matricula;
import com.escolinha.futebol.model.enums.StatusMatricula;

import java.time.LocalDate;

public record MatriculaResponseDTO(
        Long id,
        LocalDate dataMatricula,
        StatusMatricula status,
        AlunoMinDTO aluno,
        TurmaMinDTO turma
) {
    /**
     * Mapper estático para converter a Entidade Matrícula para este DTO.
     */
    public static MatriculaResponseDTO fromEntity(Matricula matricula) {
        return new MatriculaResponseDTO(
                matricula.getId(),
                matricula.getDataMatricula(),
                matricula.getStatus(),
                AlunoMinDTO.fromEntity(matricula.getAluno()),
                TurmaMinDTO.fromEntity(matricula.getTurma())
        );
    }
}