package com.escolinha.futebol.service;

import com.escolinha.futebol.model.Aluno;
import com.escolinha.futebol.model.Matricula;
import com.escolinha.futebol.model.Turma;
import com.escolinha.futebol.model.enums.StatusMatricula;
import com.escolinha.futebol.repository.MatriculaRepository;
import com.escolinha.futebol.service.exceptions.RegraNegocioException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class MatriculaServiceTest {

    @Mock
    private MatriculaRepository matriculaRepository;

    @Mock
    private AlunoService alunoService;

    @Mock
    private TurmaService turmaService;

    @InjectMocks
    private MatriculaService matriculaService;

    private Aluno aluno;
    private Turma turma;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);

        aluno = new Aluno();
        aluno.setId(1L);
        aluno.setNome("João");
        aluno.setDataNascimento(LocalDate.of(2010, 1, 1));
        aluno.setAtivo(true);

        turma = new Turma();
        turma.setId(10L);
        turma.setNome("Sub 16");
        turma.setFaixaEtariaMinima(10);
        turma.setFaixaEtariaMaxima(16);
        turma.setLimiteAlunos(20);
    }

    // ============================
    // REALIZAR MATRÍCULA
    // ============================
    @Test
    void deveRealizarMatriculaComSucesso() {

        when(alunoService.buscarPorId(1L)).thenReturn(aluno);
        when(turmaService.buscarPorId(10L)).thenReturn(turma);

        when(matriculaRepository.findByAlunoIdAndTurmaIdAndStatus(1L, 10L, StatusMatricula.ATIVA))
                .thenReturn(Optional.empty());

        when(matriculaRepository.countByTurmaIdAndStatus(10L, StatusMatricula.ATIVA))
                .thenReturn(0L);

        Matricula salva = new Matricula();
        salva.setId(100L);

        when(matriculaRepository.save(any(Matricula.class))).thenReturn(salva);

        Matricula resultado = matriculaService.realizarMatricula(1L, 10L);

        assertNotNull(resultado);
        assertEquals(100L, resultado.getId());
        verify(matriculaRepository).save(any(Matricula.class));
    }

    @Test
    void deveFalharAlunoInativo() {

        aluno.setAtivo(false);

        when(alunoService.buscarPorId(1L)).thenReturn(aluno);

        assertThrows(RegraNegocioException.class, () ->
                matriculaService.realizarMatricula(1L, 10L));
    }

    @Test
    void deveFalharMatriculaDuplicada() {

        when(alunoService.buscarPorId(1L)).thenReturn(aluno);
        when(turmaService.buscarPorId(10L)).thenReturn(turma);

        when(matriculaRepository.findByAlunoIdAndTurmaIdAndStatus(1L, 10L, StatusMatricula.ATIVA))
                .thenReturn(Optional.of(new Matricula()));

        assertThrows(RegraNegocioException.class, () ->
                matriculaService.realizarMatricula(1L, 10L));
    }

    @Test
    void deveFalharTurmaLotada() {

        when(alunoService.buscarPorId(1L)).thenReturn(aluno);
        when(turmaService.buscarPorId(10L)).thenReturn(turma);

        when(matriculaRepository.findByAlunoIdAndTurmaIdAndStatus(1L, 10L, StatusMatricula.ATIVA))
                .thenReturn(Optional.empty());

        when(matriculaRepository.countByTurmaIdAndStatus(10L, StatusMatricula.ATIVA))
                .thenReturn(20L);

        assertThrows(RegraNegocioException.class, () ->
                matriculaService.realizarMatricula(1L, 10L));
    }

    @Test
    void deveFalharIdadeMenorQuePermitida() {

        aluno.setDataNascimento(LocalDate.now().minusYears(5)); // 5 anos

        when(alunoService.buscarPorId(1L)).thenReturn(aluno);
        when(turmaService.buscarPorId(10L)).thenReturn(turma);

        assertThrows(RegraNegocioException.class, () ->
                matriculaService.realizarMatricula(1L, 10L));
    }

    @Test
    void deveFalharIdadeMaiorQuePermitida() {

        aluno.setDataNascimento(LocalDate.now().minusYears(20)); // 20 anos

        when(alunoService.buscarPorId(1L)).thenReturn(aluno);
        when(turmaService.buscarPorId(10L)).thenReturn(turma);

        assertThrows(RegraNegocioException.class, () ->
                matriculaService.realizarMatricula(1L, 10L));
    }

    // ============================
    // TRANCAR
    // ============================
    @Test
    void deveTrancarMatricula() {

        Matricula matricula = new Matricula();
        matricula.setId(1L);
        matricula.setStatus(StatusMatricula.ATIVA);

        when(matriculaRepository.findById(1L)).thenReturn(Optional.of(matricula));
        when(matriculaRepository.save(matricula)).thenReturn(matricula);

        Matricula resultado = matriculaService.trancarMatricula(1L);

        assertEquals(StatusMatricula.TRANCADA, resultado.getStatus());
        assertNotNull(resultado.getDataFim());
    }

    @Test
    void deveFalharTrancarMatriculaInexistente() {

        when(matriculaRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(RegraNegocioException.class,
                () -> matriculaService.trancarMatricula(1L));
    }

    // ============================
    // LISTAR
    // ============================
    @Test
    void deveListarPorAluno() {
        when(matriculaRepository.findByAlunoId(1L)).thenReturn(List.of(new Matricula()));

        List<Matricula> lista = matriculaService.listar(1L, null);

        assertEquals(1, lista.size());
    }

    @Test
    void deveListarPorTurma() {
        when(matriculaRepository.findByTurmaId(10L)).thenReturn(List.of(new Matricula()));

        List<Matricula> lista = matriculaService.listar(null, 10L);

        assertEquals(1, lista.size());
    }

    @Test
    void deveListarTudo() {
        when(matriculaRepository.findAll()).thenReturn(List.of(new Matricula()));

        List<Matricula> lista = matriculaService.listar(null, null);

        assertEquals(1, lista.size());
    }

    // ============================
    // MATRÍCULA ATIVA DO ALUNO
    // ============================
    @Test
    void deveBuscarMatriculaAtiva() {

        Matricula m = new Matricula();

        when(matriculaRepository.findByAlunoIdAndStatus(1L, StatusMatricula.ATIVA))
                .thenReturn(Optional.of(m));

        Matricula resultado = matriculaService.buscarMatriculaAtivaPorAluno(1L);

        assertNotNull(resultado);
    }

    @Test
    void deveFalharBuscarMatriculaAtivaNaoExiste() {

        when(matriculaRepository.findByAlunoIdAndStatus(1L, StatusMatricula.ATIVA))
                .thenReturn(Optional.empty());

        assertThrows(RegraNegocioException.class,
                () -> matriculaService.buscarMatriculaAtivaPorAluno(1L));
    }

    // ============================
    // ALTERAR STATUS
    // ============================
    @Test
    void deveAlterarStatus() {

        Matricula m = new Matricula();
        m.setId(1L);

        when(matriculaRepository.findById(1L)).thenReturn(Optional.of(m));
        when(matriculaRepository.save(m)).thenReturn(m);

        Matricula r = matriculaService.alterarStatus(1L, StatusMatricula.TRANCADA);

        assertEquals(StatusMatricula.TRANCADA, r.getStatus());
    }

    @Test
    void deveFalharAlterarStatusMatriculaNaoExiste() {

        when(matriculaRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(RegraNegocioException.class,
                () -> matriculaService.alterarStatus(1L, StatusMatricula.ATIVA));
    }
}
