package com.escolinha.futebol.dto;

import com.escolinha.futebol.model.Professor;

// O Angular não enviará o objeto Professor inteiro, apenas o id do professor que será responsável pela turma.
// DTO de Resposta (O que a API devolve):
// Quando o Angular pedir uma Turma, ele também vai querer saber o nome do professor, não apenas o ID. Vamos aninhar um DTO de Professor simplificado.

public record ProfessorMinDTO(
        Long id,
        String nome,
        String cpf
) {
    public static ProfessorMinDTO fromEntity(Professor professor) {
        if (professor == null) return null;
        return new ProfessorMinDTO(
                professor.getId(),
                professor.getNome(),
                professor.getCpf()
        );
    }
}
