package com.escolinha.futebol.dto;

public record AulaRequestDTO(
        String data,       // ex: "2025-02-10" (data específica da aula)
        String horaInicio, // ex: "14:00"
        String horaFim,    // ex: "15:30"
        Long turmaId       // id da turma selecionada
) {}
