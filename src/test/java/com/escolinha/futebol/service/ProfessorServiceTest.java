package com.escolinha.futebol.service;

import com.escolinha.futebol.model.Professor;
import com.escolinha.futebol.model.Turma;
import com.escolinha.futebol.repository.ProfessorRepository;
import com.escolinha.futebol.service.exceptions.RegraNegocioException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ProfessorServiceTest {

    @Mock
    private ProfessorRepository professorRepository;

    @InjectMocks
    private ProfessorService professorService;

    private Professor professor;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        professor = new Professor();
        professor.setId(1L);
        professor.setNome("Carlos Silva");
        professor.setCpf("12345678901");
        professor.setDataContratacao(LocalDate.now());
    }

    // ============================
    // TESTE: Criar professor
    // ============================
    @Test
    void deveCriarProfessorComSucesso() {
        when(professorRepository.findByCpf("12345678901")).thenReturn(Optional.empty());
        when(professorRepository.save(any(Professor.class))).thenReturn(professor);

        Professor criado = professorService.criarProfessor(professor);

        assertNotNull(criado);
        assertEquals("Carlos Silva", criado.getNome());
        verify(professorRepository, times(1)).save(any(Professor.class));
    }

    // ============================
    // TESTE: CPF duplicado
    // ============================
    @Test
    void deveLancarErroAoCriarProfessorComCpfDuplicado() {
        when(professorRepository.findByCpf("12345678901")).thenReturn(Optional.of(professor));

        assertThrows(RegraNegocioException.class, () -> professorService.criarProfessor(professor));
    }

    // ============================
    // TESTE: Listar todos
    // ============================
    @Test
    void deveListarTodosOsProfessores() {
        when(professorRepository.findAll()).thenReturn(Arrays.asList(professor));

        List<Professor> lista = professorService.listarTodos();

        assertEquals(1, lista.size());
    }

    // ============================
    // TESTE: Buscar por ID
    // ============================
    @Test
    void deveBuscarProfessorPorId() {
        when(professorRepository.findById(1L)).thenReturn(Optional.of(professor));

        Professor encontrado = professorService.buscarPorId(1L);

        assertNotNull(encontrado);
        assertEquals(1L, encontrado.getId());
    }

    @Test
    void deveLancarErroQuandoProfessorNaoEncontrado() {
        when(professorRepository.findById(999L)).thenReturn(Optional.empty());

        assertThrows(RegraNegocioException.class, () -> professorService.buscarPorId(999L));
    }

    // ============================
    // TESTE: Atualizar professor
    // ============================
    @Test
    void deveAtualizarProfessorComSucesso() {
        Professor novosDados = new Professor();
        novosDados.setNome("Novo Nome");
        novosDados.setCpf("12345678901");

        when(professorRepository.findById(1L)).thenReturn(Optional.of(professor));
        when(professorRepository.save(any(Professor.class))).thenReturn(professor);

        Professor atualizado = professorService.atualizarProfessor(1L, novosDados);

        assertEquals("Novo Nome", professor.getNome());
    }

    // ============================
    // TESTE: Impedir deleção com turmas
    // ============================
    @Test
    void deveLancarErroAoDeletarProfessorComTurmas() {
        Turma turma = new Turma();
        turma.setId(10L);

        professor.setTurmas(List.of(turma));

        when(professorRepository.findById(1L)).thenReturn(Optional.of(professor));

        assertThrows(RegraNegocioException.class, () -> professorService.deletarProfessor(1L));
    }

    // ============================
    // TESTE: Deletar professor
    // ============================
    @Test
    void deveDeletarProfessorComSucesso() {
        professor.setTurmas(List.of()); // sem turmas
        when(professorRepository.findById(1L)).thenReturn(Optional.of(professor));

        professorService.deletarProfessor(1L);

        verify(professorRepository, times(1)).deleteById(1L);
    }
}
