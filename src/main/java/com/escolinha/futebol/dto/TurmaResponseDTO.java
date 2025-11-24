package com.escolinha.futebol.dto;

import com.escolinha.futebol.model.Turma;
import java.util.List;
import java.util.stream.Collectors;

public record TurmaResponseDTO(
        Long id,
        String nome,
        Integer faixaEtariaMinima,
        Integer faixaEtariaMaxima,
        Integer limiteAlunos,
        ProfessorMinDTO professor,
        List<MatriculaMinDTO> matriculas
) {
    public static TurmaResponseDTO fromEntity(Turma turma) {
        return new TurmaResponseDTO(
                turma.getId(),
                turma.getNome(),
                turma.getFaixaEtariaMinima(),
                turma.getFaixaEtariaMaxima(),
                turma.getLimiteAlunos(),
                ProfessorMinDTO.fromEntity(turma.getProfessor()),
                turma.getMatriculas() == null ? null :
                        turma.getMatriculas()
                                .stream()
                                .map(MatriculaMinDTO::fromEntity)
                                .collect(Collectors.toList())
        );
    }
}
