package com.escolinha.futebol.controller;

import com.escolinha.futebol.dto.TurmaRequestDTO;
import com.escolinha.futebol.dto.TurmaResponseDTO;
import com.escolinha.futebol.model.Turma;
import com.escolinha.futebol.service.MatriculaService;
import com.escolinha.futebol.service.TurmaService;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;

import org.springframework.boot.test.mock.mockito.MockBean;

import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(TurmaController.class)
class TurmaControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TurmaService turmaService;

    @MockBean
    private MatriculaService matriculaService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void deveCriarTurma() throws Exception {
        Turma turma = new Turma();
        turma.setId(1L);
        turma.setNome("Sub-15");

        TurmaRequestDTO dto = new TurmaRequestDTO(
                "Sub-15", 10, 15, 20, 1L, List.of()
        );

        Mockito.when(turmaService.criarTurma(any(TurmaRequestDTO.class)))
                .thenReturn(turma);

        mockMvc.perform(post("/api/turmas")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.nome").value("Sub-15"));
    }

    @Test
    void deveListarTurmas() throws Exception {
        Turma turma = new Turma();
        turma.setId(1L);
        turma.setNome("Sub-15");

        Mockito.when(turmaService.listarTodas())
                .thenReturn(List.of(turma));

        mockMvc.perform(get("/api/turmas"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].nome").value("Sub-15"));
    }

    @Test
    void deveBuscarPorId() throws Exception {
        Turma turma = new Turma();
        turma.setId(1L);
        turma.setNome("Sub-15");

        Mockito.when(turmaService.buscarPorId(1L)).thenReturn(turma);

        mockMvc.perform(get("/api/turmas/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1));
    }

    @Test
    void deveDeletarTurma() throws Exception {
        mockMvc.perform(delete("/api/turmas/1"))
                .andExpect(status().isNoContent());

        Mockito.verify(turmaService).deletarTurma(1L);
    }
}
