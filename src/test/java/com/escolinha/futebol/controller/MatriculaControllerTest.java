package com.escolinha.futebol.controller;

import com.escolinha.futebol.dto.MatriculaMinDTO;
import com.escolinha.futebol.model.Aluno;
import com.escolinha.futebol.model.Turma;
import com.escolinha.futebol.model.Matricula;
import com.escolinha.futebol.model.enums.StatusMatricula;
import com.escolinha.futebol.service.MatriculaService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(MatriculaController.class)
class MatriculaControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private MatriculaService matriculaService;

    // ------------------------
    // MÉTODOS AUXILIARES
    // ------------------------
    private Matricula criarMatriculaMock() {
        Aluno aluno = new Aluno();
        aluno.setId(1L);
        aluno.setCodigoAluno("ALN123");

        Turma turma = new Turma();
        turma.setId(10L);

        Matricula m = new Matricula();
        m.setId(1L);
        m.setAluno(aluno);
        m.setTurma(turma);
        m.setDataMatricula(LocalDate.now());
        m.setStatus(StatusMatricula.ATIVA);

        return m;
    }

    // ------------------------
    // TESTE: Criar matrícula
    // ------------------------
    @Test
    void deveCriarMatricula() throws Exception {

        // Criando aluno mockado para compor o DTO
        Aluno aluno = new Aluno();
        aluno.setId(1L);
        aluno.setNome("João da Silva");
        aluno.setCodigoAluno("ALN123");

        // Criando matrícula fake
        Matricula m = new Matricula();
        m.setId(1L);
        m.setDataMatricula(LocalDate.now());
        m.setAluno(aluno);

        Mockito.when(matriculaService.realizarMatricula(1L, 10L))
                .thenReturn(m);

        mockMvc.perform(post("/api/matriculas")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                    {
                        "alunoId": 1,
                        "turmaId": 10
                    }
                    """))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.codigoAluno").value("ALN123"))
                .andExpect(jsonPath("$.alunoNome").value("João da Silva"))
                .andExpect(jsonPath("$.status").value("ATIVA"));
    }

    // ------------------------
    // TESTE: Listar matrículas
    // ------------------------
    @Test
    void deveListarMatriculas() throws Exception {

        Matricula m = criarMatriculaMock();

        Mockito.when(matriculaService.listar(null, null))
                .thenReturn(List.of(m));

        mockMvc.perform(get("/api/matriculas"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1));
    }

    // ------------------------
    // TESTE: Trancar matrícula
    // ------------------------
    @Test
    void deveTrancarMatricula() throws Exception {

        Matricula m = criarMatriculaMock();
        m.setStatus(StatusMatricula.TRANCADA);

        Mockito.when(matriculaService.trancarMatricula(1L))
                .thenReturn(m);

        mockMvc.perform(put("/api/matriculas/1/trancar"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("TRANCADA"));
    }

    // ------------------------
    // TESTE: Alterar status
    // ------------------------
    @Test
    void deveAlterarStatus() throws Exception {

        Matricula m = criarMatriculaMock();
        m.setStatus(StatusMatricula.TRANCADA);

        Mockito.when(matriculaService.alterarStatus(1L, StatusMatricula.TRANCADA))
                .thenReturn(m);

        mockMvc.perform(put("/api/matriculas/1/status?status=TRANCADA"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("TRANCADA"));
    }
}
