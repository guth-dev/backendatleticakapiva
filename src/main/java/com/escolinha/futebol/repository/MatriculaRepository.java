package com.escolinha.futebol.repository;

import com.escolinha.futebol.model.Matricula;
import com.escolinha.futebol.model.enums.StatusMatricula;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MatriculaRepository extends JpaRepository<Matricula, Long> {

    Optional<Matricula> findByAlunoIdAndTurmaIdAndStatus(Long alunoId, Long turmaId, StatusMatricula status);

    List<Matricula> findByAlunoId(Long alunoId);

    List<Matricula> findByTurmaId(Long turmaId);

    Long countByTurmaIdAndStatus(Long turmaId, StatusMatricula status);
}
