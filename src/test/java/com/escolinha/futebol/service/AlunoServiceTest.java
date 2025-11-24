package com.escolinha.futebol.service;

import com.escolinha.futebol.model.Aluno;
import com.escolinha.futebol.model.Matricula;
import com.escolinha.futebol.repository.AlunoRepository;
import com.escolinha.futebol.repository.MatriculaRepository;
import com.escolinha.futebol.service.exceptions.RegraNegocioException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AlunoServiceTest {

    @InjectMocks
    private AlunoService alunoService;

    @Mock
    private AlunoRepository alunoRepository;

    @Mock
    private MatriculaRepository matriculaRepository;

    // --- TESTES DE CRIAÇÃO (CREATE) ---

    @Test
    @DisplayName("Deve criar aluno com sucesso e gerar código e matrícula inicial")
    void deveCriarAlunoComSucesso() {
        // 1. Cenario (Arrange)
        Aluno alunoParaSalvar = new Aluno();
        alunoParaSalvar.setNome("Joãozinho");
        alunoParaSalvar.setCpfResponsavel("12345678900");

        // Simula que NÃO existe ninguém com esse CPF
        when(alunoRepository.findByCpfResponsavel("12345678900")).thenReturn(Optional.empty());

        // Simula a busca do último código para gerar o novo (ex: retorna null para ser o primeiro do ano)
        when(alunoRepository.buscarUltimaMatriculaDoAno(anyString())).thenReturn(null);

        // Simula o salvamento retornando o próprio objeto
        when(alunoRepository.save(any(Aluno.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // 2. Ação (Act)
        Aluno alunoSalvo = alunoService.criarAluno(alunoParaSalvar);

        // 3. Verificação (Assert)
        assertNotNull(alunoSalvo.getCodigoAluno(), "O código do aluno deve ser gerado automaticamente");
        assertTrue(alunoSalvo.getCodigoAluno().contains("-0001"), "Como é o primeiro, deve terminar em 0001");

        // Verifica se o método save do repositório de matrícula foi chamado 1 vez
        verify(matriculaRepository, times(1)).save(any(Matricula.class));
    }

    @Test
    @DisplayName("Deve lançar exceção ao tentar cadastrar CPF duplicado")
    void deveLancarErroCpfDuplicado() {
        // 1. Cenario
        Aluno alunoNovo = new Aluno();
        alunoNovo.setCpfResponsavel("11122233344");

        Aluno alunoExistente = new Aluno();
        alunoExistente.setId(1L);
        alunoExistente.setCpfResponsavel("11122233344");

        // Simula que JÁ existe alguém com esse CPF
        when(alunoRepository.findByCpfResponsavel("11122233344")).thenReturn(Optional.of(alunoExistente));

        // 2. e 3. Ação e Verificação
        assertThrows(RegraNegocioException.class, () -> {
            alunoService.criarAluno(alunoNovo);
        });

        // Garante que NUNCA tentou salvar nada se deu erro antes
        verify(alunoRepository, never()).save(any());
    }

    // --- TESTES DE LEITURA (READ) ---

    @Test
    @DisplayName("Deve buscar aluno por ID com sucesso")
    void deveBuscarPorId() {
        Aluno aluno = new Aluno();
        aluno.setId(1L);
        aluno.setNome("Maria");

        when(alunoRepository.findById(1L)).thenReturn(Optional.of(aluno));

        Aluno encontrado = alunoService.buscarPorId(1L);

        assertEquals("Maria", encontrado.getNome());
    }

    @Test
    @DisplayName("Deve lançar erro ao buscar ID inexistente")
    void deveFalharBuscarIdInexistente() {
        when(alunoRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(RegraNegocioException.class, () -> {
            alunoService.buscarPorId(99L);
        });
    }

    // --- TESTES DE DELETE (SOFT DELETE) ---

    @Test
    @DisplayName("Deve desativar aluno (Soft Delete)")
    void deveDesativarAluno() {
        // Cenario
        Aluno alunoAtivo = new Aluno();
        alunoAtivo.setId(10L);
        alunoAtivo.setAtivo(true);

        when(alunoRepository.findById(10L)).thenReturn(Optional.of(alunoAtivo));

        // Ação
        alunoService.desativarAluno(10L);

        // Verificação
        assertFalse(alunoAtivo.getAtivo()); // O objeto deve ter mudado para false
        verify(alunoRepository, times(1)).save(alunoAtivo); // Deve ter salvo a alteração
    }
}