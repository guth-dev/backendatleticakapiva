package com.escolinha.futebol.dto;

import com.escolinha.futebol.model.Aluno;
import com.escolinha.futebol.model.Matricula;
import com.escolinha.futebol.model.Turma;
import com.escolinha.futebol.model.Professor;

public record AlunoPainelResponseDTO(
        Long alunoId,
        String nomeAluno,
        String codigoAluno,
        String nomeResponsavel,
        String telefoneResponsavel,
        Long turmaId,
        String nomeTurma,
        Long professorId,
        String nomeProfessor
) {

    public static AlunoPainelResponseDTO fromEntity(Aluno aluno, Matricula matricula) {

        Turma turma = matricula.getTurma();
        Professor professor = turma.getProfessor();

        return new AlunoPainelResponseDTO(
                aluno.getId(),
                aluno.getNome(),
                aluno.getCodigoAluno(),
                aluno.getNomeResponsavel(),
                aluno.getTelefoneResponsavel(),
                turma.getId(),
                turma.getNome(),
                professor.getId(),
                professor.getNome()
        );
    }
}
