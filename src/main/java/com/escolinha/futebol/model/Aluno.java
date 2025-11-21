package com.escolinha.futebol.model;

import jakarta.validation.constraints.Email;
import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.Generated;
import java.util.List;

import java.time.LocalDate;

@Entity // classe é uma entidade JPA
@Data // anotação lombok  gera getters, setters, toString, equals e hashCode
@Table(name = "alunos")
public class Aluno {

    // Dados do Aluno

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Configura a geração automática de ID
    private Long id;

    @Column(nullable = false)
    private String nome;

    @Column (nullable = false)
    private LocalDate dataNascimento;

    // Dados do responsavel

    @Column (nullable = false)
    private String nomeResponsavel;

    @Column (nullable = false, unique = true)
    @Size(min = 11, max = 11, message = "O CPF deve ter 11 dígitos.")
    private String cpfResponsavel;

    // Contato
    @Email(message = "O formato do e-mail é inválido.")
    @Column (nullable = false)
    private String emailResponsavel;

    @Column (nullable = false)
    private String telefoneResponsavel;


    @Column(nullable = false)
    private LocalDate dataMatricula = LocalDate.now(); // padrao dataAtual

    @Column(nullable = false)
    private Boolean ativo = true; // Aluno com matrícula ativa por padrão

    // Relacionamento com Matricula (OneToMany)
    @OneToMany(mappedBy = "aluno", cascade = CascadeType.ALL)
    private List<Matricula> matriculas;
}
