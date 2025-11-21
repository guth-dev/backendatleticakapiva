package com.escolinha.futebol.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;

// Usamos Lombok para getters/setters (ou @Data)
// Este DTO NÃO TEM ID. O cliente não pode definir o ID.
public record AlunoRequestDTO(
        @NotBlank(message = "O nome do aluno é obrigatório.")
        String nome,

        @NotNull(message = "A data de nascimento é obrigatória.")
        LocalDate dataNascimento,

        @NotBlank(message = "O nome do responsável é obrigatório.")
        String nomeResponsavel,

        @NotBlank(message = "O CPF do responsável é obrigatório.")
        @Size(min = 11, max = 11, message = "CPF deve ter 11 dígitos.")
        String cpfResponsavel,

        @NotBlank(message = "O e-mail do responsável é obrigatório.")
        @Email(message = "Formato de e-mail inválido.")
        String emailResponsavel,

        @NotBlank(message = "O telefone do responsável é obrigatório.")
        String telefoneResponsavel
) {
    // Nota: Estamos usando um "Record" do Java 16+.
    // Se preferir, pode usar uma "class" normal com @Data do Lombok.
}