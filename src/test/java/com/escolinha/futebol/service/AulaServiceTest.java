package com.escolinha.futebol.service;

import com.escolinha.futebol.model.Aula;
import com.escolinha.futebol.model.Professor;
import com.escolinha.futebol.model.Turma;
import com.escolinha.futebol.repository.AulaRepository;
import com.escolinha.futebol.repository.TurmaRepository;
import com.escolinha.futebol.service.exceptions.RegraNegocioException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AulaServiceTest {

    @Mock
    private AulaRepository aulaRepository;

    @Mock
    private TurmaRepository turmaRepository;

    @InjectMocks
    private AulaService aulaService;

    private Turma turma;
    private Professor professor;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);

        professor = new Professor();
        professor.setId(1L);
        professor.setNome("Rogério");

        turma = new Turma();
        turma.setId(10L);
        turma.setNome("Sub - 16");
        turma.setProfessor(professor);
    }

    // ============================
    // LISTAR
    // ============================
    @Test
    void deveListarAulas() {
        Aula aula = new Aula();
        when(aulaRepository.findAll()).thenReturn(List.of(aula));

        List<Aula> lista = aulaService.listar();

        assertEquals(1, lista.size());
        verify(aulaRepository, times(1)).findAll();
    }

    // ============================
    // CRIAR
    // ============================
    @Test
    void deveCriarAulaComSucesso() {

        Aula dados = new Aula();
        dados.setData("2025-01-20");
        dados.setHoraInicio("18:00");
        dados.setHoraFim("19:00");

        when(turmaRepository.findById(10L)).thenReturn(Optional.of(turma));

        Aula salva = new Aula();
        salva.setId(1L);
        when(aulaRepository.save(any(Aula.class))).thenReturn(salva);

        Aula resultado = aulaService.criar(dados, 10L);

        assertNotNull(resultado);
        verify(aulaRepository).save(any(Aula.class));
    }

    @Test
    void deveFalharAoCriarAulaTurmaNaoExiste() {

        Aula dados = new Aula();

        when(turmaRepository.findById(10L)).thenReturn(Optional.empty());

        assertThrows(RegraNegocioException.class,
                () -> aulaService.criar(dados, 10L));
    }

    // ============================
    // EDITAR
    // ============================
    @Test
    void deveEditarAulaComSucesso() {

        Aula aulaExistente = new Aula();
        aulaExistente.setId(1L);

        Aula dados = new Aula();
        dados.setData("2025-02-11");
        dados.setHoraInicio("19:00");
        dados.setHoraFim("20:00");

        when(aulaRepository.findById(1L)).thenReturn(Optional.of(aulaExistente));
        when(turmaRepository.findById(10L)).thenReturn(Optional.of(turma));

        Aula salva = new Aula();
        salva.setId(1L);

        when(aulaRepository.save(any(Aula.class))).thenReturn(salva);

        Aula resultado = aulaService.editar(1L, dados, 10L);

        assertNotNull(resultado);
        verify(aulaRepository).save(any(Aula.class));
    }

    @Test
    void deveFalharAoEditarAulaInexistente() {

        Aula dados = new Aula();

        when(aulaRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(RegraNegocioException.class,
                () -> aulaService.editar(1L, dados, 10L));
    }

    @Test
    void deveFalharAoEditarAulaComTurmaInexistente() {

        Aula aulaExistente = new Aula();
        when(aulaRepository.findById(1L)).thenReturn(Optional.of(aulaExistente));

        when(turmaRepository.findById(10L)).thenReturn(Optional.empty());

        Aula dados = new Aula();

        assertThrows(RegraNegocioException.class,
                () -> aulaService.editar(1L, dados, 10L));
    }

    // ============================
    // DELETAR
    // ============================
    @Test
    void deveDeletarAulaComSucesso() {
        aulaService.deletar(1L);
        verify(aulaRepository).deleteById(1L);
    }
}
