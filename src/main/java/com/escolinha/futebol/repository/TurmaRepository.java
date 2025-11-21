package com.escolinha.futebol.repository;

import com.escolinha.futebol.model.Turma;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface TurmaRepository extends JpaRepository<Turma, Long> {

}