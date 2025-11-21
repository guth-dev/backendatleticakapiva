package com.escolinha.futebol.dto;

import com.escolinha.futebol.model.Aula;

public record AulaResponseDTO(
        Long id,
        String titulo,
        String data,
        String hora
) {
    public static AulaResponseDTO fromEntity(Aula aula) {
        return new AulaResponseDTO(
                aula.getId(),
                aula.getTitulo(),
                aula.getData(),
                aula.getHora()
        );
    }
}
