package com.escolinha.futebol.dto;

import com.escolinha.futebol.model.Aluno;

import java.time.LocalDate;

// Este DTO TEM ID e os campos controlados pelo servidor.
public record AlunoResponseDTO(
        Long id,
        String nome,
        LocalDate dataNascimento,
        String nomeResponsavel,
        String emailResponsavel,
        String telefoneResponsavel,
        LocalDate dataMatricula,
        Boolean ativo
) {
    /**
     * "Mapper" estático simples: Converte a Entidade Aluno para este DTO.
     */
    public static AlunoResponseDTO fromEntity(Aluno aluno) {
        return new AlunoResponseDTO(
                aluno.getId(),
                aluno.getNome(),
                aluno.getDataNascimento(),
                aluno.getNomeResponsavel(),
                aluno.getEmailResponsavel(),
                aluno.getTelefoneResponsavel(),
                aluno.getDataMatricula(),
                aluno.getAtivo()
        );
    }
}