package com.escolinha.futebol.controller;

import com.escolinha.futebol.dto.ProfessorRequestDTO;
import com.escolinha.futebol.model.Professor;
import com.escolinha.futebol.service.ProfessorService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.LocalDate;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class ProfessorControllerTest {

    @Mock
    private ProfessorService professorService;

    @InjectMocks
    private ProfessorController professorController;

    private MockMvc mockMvc;
    private ObjectMapper mapper = new ObjectMapper();

    private Professor professor;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(professorController).build();

        professor = new Professor();
        professor.setId(1L);
        professor.setNome("Carlos");
        professor.setCpf("12345678901");
        professor.setDataContratacao(LocalDate.now());
    }

    // ============================
    // POST
    // ============================
    @Test
    void deveCriarProfessor() throws Exception {
        ProfessorRequestDTO dto = new ProfessorRequestDTO("Carlos", "123.456.789-01");

        when(professorService.criarProfessor(any(Professor.class))).thenReturn(professor);

        mockMvc.perform(post("/api/professores")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(dto)))
                .andExpect(status().isCreated());
    }

    // ============================
    // GET – listar
    // ============================
    @Test
    void deveListarProfessores() throws Exception {
        when(professorService.listarTodos()).thenReturn(List.of(professor));

        mockMvc.perform(get("/api/professores"))
                .andExpect(status().isOk());
    }

    // ============================
    // GET por ID
    // ============================
    @Test
    void deveBuscarProfessorPorId() throws Exception {
        when(professorService.buscarPorId(1L)).thenReturn(professor);

        mockMvc.perform(get("/api/professores/1"))
                .andExpect(status().isOk());
    }

    // ============================
    // PUT atualizar
    // ============================
    @Test
    void deveAtualizarProfessor() throws Exception {
        ProfessorRequestDTO dto = new ProfessorRequestDTO("Atualizado", "123.456.789-01");

        when(professorService.atualizarProfessor(eq(1L), any(Professor.class))).thenReturn(professor);

        mockMvc.perform(put("/api/professores/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(dto)))
                .andExpect(status().isOk());
    }

    // ============================
    // DELETE
    // ============================
    @Test
    void deveDeletarProfessor() throws Exception {
        doNothing().when(professorService).deletarProfessor(1L);

        mockMvc.perform(delete("/api/professores/1"))
                .andExpect(status().isNoContent());
    }
}
