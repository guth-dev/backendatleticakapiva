package com.escolinha.futebol.repository;

import com.escolinha.futebol.model.Matricula;
import com.escolinha.futebol.model.enums.StatusMatricula;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MatriculaRepository extends JpaRepository<Matricula, Long> {

    // Verificar se já existe matrícula ativa do aluno na turma
    Optional<Matricula> findByAlunoIdAndTurmaIdAndStatus(
            Long alunoId,
            Long turmaId,
            StatusMatricula status
    );

    // Listar matrículas de um aluno específico
    List<Matricula> findByAlunoId(Long alunoId);

    // Listar matrículas de uma turma específica
    List<Matricula> findByTurmaId(Long turmaId);

    // Contar matrículas ativas para saber se a turma lotou
    Long countByTurmaIdAndStatus(Long turmaId, StatusMatricula status);
}
