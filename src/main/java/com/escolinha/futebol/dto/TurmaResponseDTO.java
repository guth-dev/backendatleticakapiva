package com.escolinha.futebol.dto;

import com.escolinha.futebol.model.Turma;

import java.time.LocalTime;
import java.util.List;
import java.util.stream.Collectors;

public record TurmaResponseDTO(
        Long id,
        String nome,
        Integer faixaEtariaMinima,
        Integer faixaEtariaMaxima,
        String diaSemana,
        LocalTime horarioInicio,
        LocalTime horarioFim,
        Integer limiteAlunos,
        ProfessorMinDTO professor,
        List<MatriculaMinDTO> matriculas // <-- ADICIONADO
) {
    public static TurmaResponseDTO fromEntity(Turma turma) {
        return new TurmaResponseDTO(
                turma.getId(),
                turma.getNome(),
                turma.getFaixaEtariaMinima(),
                turma.getFaixaEtariaMaxima(),
                turma.getDiaSemana(),
                turma.getHorarioInicio(),
                turma.getHorarioFim(),
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
