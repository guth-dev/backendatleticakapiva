package com.escolinha.futebol.dto;

import com.escolinha.futebol.model.Aluno;

import java.time.LocalDate;

// Este DTO TEM ID e os campos controlados pelo servidor.
public record AlunoResponseDTO(
        Long id,
        String codigoAluno,      // <--- ADICIONADO
        String nome,
        LocalDate dataNascimento,
        String nomeResponsavel,
        String cpfResponsavel,
        String emailResponsavel,
        String telefoneResponsavel,
        LocalDate dataMatricula,
        Boolean ativo
) {
    /**
     * Converte a Entidade Aluno para este DTO.
     */
    public static AlunoResponseDTO fromEntity(Aluno aluno) {
        return new AlunoResponseDTO(
                aluno.getId(),
                aluno.getCodigoAluno(),      // <--- NOVO CAMPO
                aluno.getNome(),
                aluno.getDataNascimento(),
                aluno.getNomeResponsavel(),
                aluno.getCpfResponsavel(),
                aluno.getEmailResponsavel(),
                aluno.getTelefoneResponsavel(),
                aluno.getDataMatricula(),
                aluno.getAtivo()
        );
    }
}
