package com.escolinha.futebol.dto;

import com.escolinha.futebol.model.Aluno;

import java.time.LocalDate;

public record AlunoResponseDTO(
        Long id,
        String codigoAluno,
        String nome,
        LocalDate dataNascimento,
        String nomeResponsavel,
        String cpfResponsavel,
        String emailResponsavel,
        String telefoneResponsavel,
        LocalDate dataMatricula,
        Boolean ativo
) {
    public static AlunoResponseDTO fromEntity(Aluno aluno) {
        return new AlunoResponseDTO(
                aluno.getId(),
                aluno.getCodigoAluno(),
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
