package com.escolinha.futebol.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

// Usando 'record' para simplicidade
public record ProfessorRequestDTO(
        @NotBlank(message = "O nome do professor é obrigatório.")
        String nome,

        @NotBlank(message = "O CPF do professor é obrigatório.")
        String cpf) {

}