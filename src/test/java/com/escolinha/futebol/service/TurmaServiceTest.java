package com.escolinha.futebol.service;

import com.escolinha.futebol.dto.TurmaRequestDTO;
import com.escolinha.futebol.model.Matricula;
import com.escolinha.futebol.model.Professor;
import com.escolinha.futebol.model.Turma;
import com.escolinha.futebol.repository.ProfessorRepository;
import com.escolinha.futebol.repository.TurmaRepository;
import com.escolinha.futebol.service.exceptions.RegraNegocioException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import com.escolinha.futebol.model.enums.StatusMatricula;


import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class TurmaServiceTest {

    @Mock
    private TurmaRepository turmaRepository;

    @Mock
    private ProfessorRepository professorRepository;

    @InjectMocks
    private TurmaService turmaService;

    private Professor professor;
    private Turma turma;
    private TurmaRequestDTO turmaRequest;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);

        professor = new Professor();
        professor.setId(1L);
        professor.setNome("João");

        turma = new Turma();
        turma.setId(10L);
        turma.setNome("Sub-15");
        turma.setFaixaEtariaMinima(10);
        turma.setFaixaEtariaMaxima(15);
        turma.setLimiteAlunos(20);
        turma.setProfessor(professor);

        turmaRequest = new TurmaRequestDTO(
                "Sub-15",
                10,
                15,
                20,
                1L,
                null
        );
    }

    @Test
    void deveCriarTurmaComSucesso() {
        when(professorRepository.findById(1L)).thenReturn(Optional.of(professor));
        when(turmaRepository.save(any(Turma.class))).thenReturn(turma);

        Turma resultado = turmaService.criarTurma(turmaRequest);

        assertNotNull(resultado);
        assertEquals("Sub-15", resultado.getNome());
        verify(turmaRepository, times(1)).save(any(Turma.class));
    }

    @Test
    void deveLancarExcecaoQuandoProfessorNaoExiste() {
        when(professorRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(RegraNegocioException.class,
                () -> turmaService.criarTurma(turmaRequest));
    }

    @Test
    void deveListarTodas() {
        when(turmaRepository.findAll()).thenReturn(List.of(turma));

        List<Turma> lista = turmaService.listarTodas();

        assertEquals(1, lista.size());
        verify(turmaRepository, times(1)).findAll();
    }

    @Test
    void deveBuscarPorId() {
        when(turmaRepository.findById(10L)).thenReturn(Optional.of(turma));

        Turma resultado = turmaService.buscarPorId(10L);

        assertEquals(10L, resultado.getId());
    }

    @Test
    void deveLancarErroAoBuscarPorIdInexistente() {
        when(turmaRepository.findById(10L)).thenReturn(Optional.empty());

        assertThrows(RegraNegocioException.class,
                () -> turmaService.buscarPorId(10L));
    }

    @Test
    void deveDeletarTurmaQuandoNaoTemMatriculasAtivas() {
        turma.setMatriculas(new ArrayList<>()); // sem matrículas

        when(turmaRepository.findById(10L)).thenReturn(Optional.of(turma));

        turmaService.deletarTurma(10L);

        verify(turmaRepository, times(1)).deleteById(10L);
    }

    @Test
    void deveLancarErroAoDeletarTurmaComMatriculaAtiva() {
        Matricula matricula = new Matricula();
        matricula.setStatus(StatusMatricula.ATIVA);

        turma.setMatriculas(List.of(matricula));

        when(turmaRepository.findById(10L)).thenReturn(Optional.of(turma));

        assertThrows(RegraNegocioException.class,
                () -> turmaService.deletarTurma(10L));
    }
}
