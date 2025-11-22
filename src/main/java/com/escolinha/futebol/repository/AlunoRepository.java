package com.escolinha.futebol.repository;

import com.escolinha.futebol.model.Aluno;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AlunoRepository extends JpaRepository<Aluno, Long> {

    Optional<Aluno> findByCpfResponsavel(String cpfResponsavel);

    @Query(value = """
        SELECT a.codigo_aluno
        FROM alunos a
        WHERE a.codigo_aluno LIKE CONCAT(:ano, '-%')
        ORDER BY a.codigo_aluno DESC
        LIMIT 1
    """, nativeQuery = true)
    String buscarUltimaMatriculaDoAno(String ano);
}
