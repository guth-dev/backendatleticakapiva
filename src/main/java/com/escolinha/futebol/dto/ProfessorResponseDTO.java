package com.escolinha.futebol.dto;

import com.escolinha.futebol.model.Professor;
import java.time.LocalDate;

public record ProfessorResponseDTO(
        Long id,
        String nome,
        String cpf,              // 👉 ADICIONADO
        LocalDate dataContratacao
) {
    public static ProfessorResponseDTO fromEntity(Professor professor) {
        return new ProfessorResponseDTO(
                professor.getId(),
                professor.getNome(),
                professor.getCpf(),          // 👉 ADICIONADO
                professor.getDataContratacao()
        );
    }
}