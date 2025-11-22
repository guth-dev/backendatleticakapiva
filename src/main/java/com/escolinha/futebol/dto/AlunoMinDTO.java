package com.escolinha.futebol.dto;

import com.escolinha.futebol.model.Aluno;

public record AlunoMinDTO(
        Long id,
        String nome
) {
    public static AlunoMinDTO fromEntity(Aluno aluno) {
        if (aluno == null) return null;
        return new AlunoMinDTO(aluno.getId(), aluno.getNome());
    }
}
