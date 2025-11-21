package com.escolinha.futebol.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;
import java.time.LocalDate;
import java.util.List;

@Entity
@Data
@Table(name = "professores")
public class Professor {


    // Dados do professor
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "O nome do professor é obrigatório.")
    @Column(nullable = false)
    private String nome;

    @NotBlank(message = "O CPF do professor é obrigatório.")
    @Column(nullable = false, unique = true)
    private String cpf;

    private LocalDate dataContratacao;



    // --- Relacionamento 1:N ---
    // Um professor pode ter várias turmas
    // "mappedBy" indica que o relacionamento já foi mapeado
    // pelo campo "professor" na classe Turma.
    @OneToMany(mappedBy = "professor")
    private List<Turma> turmas;
}