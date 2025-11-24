package com.escolinha.futebol.dto;

import com.escolinha.futebol.model.Aula;

public record AulaResponseDTO(
        Long id,
        String data,
        String horaInicio,
        String horaFim,
        String titulo,
        Long turmaId
) {
    public static AulaResponseDTO fromEntity(Aula aula) {
        return new AulaResponseDTO(
                aula.getId(),
                aula.getData(),
                aula.getHoraInicio(),
                aula.getHoraFim(),
                aula.getTitulo(),
                aula.getTurma() != null ? aula.getTurma().getId() : null
        );
    }
}
