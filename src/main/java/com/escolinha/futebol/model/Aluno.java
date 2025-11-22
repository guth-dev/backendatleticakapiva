package com.escolinha.futebol.model;

import jakarta.validation.constraints.Email;
import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.Data;
import java.util.List;
import java.time.LocalDate;

@Entity
@Data
@Table(name = "alunos")
public class Aluno {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Código automático: ANO + sequencial (ex: 2025-0001)
    @Column(nullable = false, unique = true, length = 9)
    private String codigoAluno;

    @Column(nullable = false)
    private String nome;

    @Column(nullable = false)
    private LocalDate dataNascimento;

    // Dados do responsável
    @Column(nullable = false)
    private String nomeResponsavel;

    @Column(nullable = false, unique = true)
    @Size(min = 11, max = 11, message = "O CPF deve ter 11 dígitos.")
    private String cpfResponsavel;

    // Contato
    @Email(message = "O formato do e-mail é inválido.")
    @Column(nullable = false)
    private String emailResponsavel;

    @Column(nullable = false)
    private String telefoneResponsavel;

    @Column(nullable = false)
    private LocalDate dataMatricula = LocalDate.now();

    @Column(nullable = false)
    private Boolean ativo = true;

    // Relacionamento com Matricula
    @OneToMany(mappedBy = "aluno", cascade = CascadeType.ALL)
    private List<Matricula> matriculas;
}
