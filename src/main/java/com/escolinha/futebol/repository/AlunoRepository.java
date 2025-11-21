package com.escolinha.futebol.repository;

import com.escolinha.futebol.model.Aluno;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional; // <-- Importe o Optional

@Repository
public interface AlunoRepository extends JpaRepository<Aluno, Long> {
    // JpaRepository<TipoDaEntidade, TipoDoId>


    // =========================================================
    // !! ADICIONE ESTA LINHA !!
    // =========================================================

    /**
     * O Spring Data JPA "lê" o nome deste método e automaticamente
     * cria a consulta SQL: "SELECT * FROM alunos WHERE cpf_responsavel = ?"
     */
    Optional<Aluno> findByCpfResponsavel(String cpfResponsavel);

    // =========================================================

}