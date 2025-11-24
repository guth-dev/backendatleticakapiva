package com.escolinha.futebol.controller;

import com.escolinha.futebol.dto.AlunoRequestDTO;
import com.escolinha.futebol.model.Aluno;
import com.escolinha.futebol.service.AlunoService;
import com.escolinha.futebol.service.MatriculaService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(AlunoController.class) // Foca apenas neste controller
class AlunoControllerTest {

    @Autowired
    private MockMvc mockMvc; // O "Postman" simulado

    @MockBean
    private AlunoService alunoService; // Mockamos o service (a cozinha)

    @MockBean
    private MatriculaService matriculaService; // O Controller pede esse service também

    @Autowired
    private ObjectMapper objectMapper; // Para transformar Objetos em JSON

    @Test
    @DisplayName("Deve retornar status 201 Created ao criar aluno válido")
    void deveCriarAlunoCorretamente() throws Exception {
        // 1. Cenário (Input)
        // Criamos o DTO (o JSON de entrada)
        AlunoRequestDTO requestDTO = new AlunoRequestDTO(
                "Joãozinho",
                LocalDate.of(2015, 5, 20),
                "Pai do João",
                "12345678900",
                "pai@email.com",
                "11999999999"
        );

        // Criamos o que o Service vai devolver (Output simulado)
        Aluno alunoSalvo = new Aluno();
        alunoSalvo.setId(1L);
        alunoSalvo.setNome("Joãozinho");
        alunoSalvo.setCodigoAluno("2025-0001"); // O service geraria isso

        // Ensinamos o Mock a responder
        when(alunoService.criarAluno(any(Aluno.class))).thenReturn(alunoSalvo);

        // 2. Ação e Validação (O "Postman" rodando)
        mockMvc.perform(post("/api/alunos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDTO))) // Transforma o DTO em JSON string
                .andExpect(status().isCreated()) // Espera HTTP 201
                .andExpect(jsonPath("$.nome").value("Joãozinho")) // Verifica o JSON de resposta
                .andExpect(jsonPath("$.codigoAluno").value("2025-0001"));
    }

    @Test
    @DisplayName("Deve retornar status 200 OK ao buscar aluno por ID")
    void deveBuscarAlunoPorId() throws Exception {
        Aluno aluno = new Aluno();
        aluno.setId(10L);
        aluno.setNome("Maria");

        when(alunoService.buscarPorId(10L)).thenReturn(aluno);

        mockMvc.perform(get("/api/alunos/{id}", 10L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nome").value("Maria"));
    }
}