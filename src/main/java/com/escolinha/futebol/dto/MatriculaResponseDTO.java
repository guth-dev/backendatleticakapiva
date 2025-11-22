package com.escolinha.futebol.dto;

import com.escolinha.futebol.model.Matricula;

import java.time.LocalDate;

public record MatriculaResponseDTO(
        Long id,
        String numeroMatricula,  // códigoAluno do Aluno
        String nomeAluno,
        LocalDate dataMatricula,
        String status
) {
    public static MatriculaResponseDTO fromEntity(Matricula matricula) {
        return new MatriculaResponseDTO(
                matricula.getId(),
                matricula.getAluno().getCodigoAluno(), // pega o código do aluno
                matricula.getAluno().getNome(),        // pega o nome do aluno
                matricula.getDataMatricula(),
                matricula.getStatus().name()
        );
    }
}
