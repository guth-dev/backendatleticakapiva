package com.escolinha.futebol.controller;

import com.escolinha.futebol.dto.AulaRequestDTO;
import com.escolinha.futebol.model.Aula;
import com.escolinha.futebol.service.AulaService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(AulaController.class)
class AulaControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AulaService aulaService;

    @Test
    void deveListarAulas() throws Exception {
        Mockito.when(aulaService.listar()).thenReturn(List.of(new Aula()));

        mockMvc.perform(get("/api/aulas"))
                .andExpect(status().isOk());
    }

    @Test
    void deveCriarAula() throws Exception {

        Aula aula = new Aula();
        aula.setId(1L);

        Mockito.when(aulaService.criar(any(Aula.class), eq(10L)))
                .thenReturn(aula);

        mockMvc.perform(post("/api/aulas")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                        {
                            "data": "2025-01-20",
                            "horaInicio": "18:00",
                            "horaFim": "19:00",
                            "turmaId": 10
                        }
                        """))
                .andExpect(status().isOk());
    }

    @Test
    void deveEditarAula() throws Exception {

        Aula aula = new Aula();
        aula.setId(1L);

        Mockito.when(aulaService.editar(eq(1L), any(Aula.class), eq(10L)))
                .thenReturn(aula);

        mockMvc.perform(put("/api/aulas/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                        {
                            "data": "2025-02-10",
                            "horaInicio": "18:30",
                            "horaFim": "19:30",
                            "turmaId": 10
                        }
                        """))
                .andExpect(status().isOk());
    }

    @Test
    void deveDeletarAula() throws Exception {
        mockMvc.perform(delete("/api/aulas/1"))
                .andExpect(status().isOk());
    }
}
