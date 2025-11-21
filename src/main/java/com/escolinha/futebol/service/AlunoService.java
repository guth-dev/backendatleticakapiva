package com.escolinha.futebol.service;

import com.escolinha.futebol.model.Aluno;
import com.escolinha.futebol.repository.AlunoRepository;
import com.escolinha.futebol.service.exceptions.RegraNegocioException;
import lombok.RequiredArgsConstructor; // Import do Lombok
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service // 1. Marca a classe como um Serviço do Spring
@RequiredArgsConstructor // 2. (Lombok) Cria um construtor com campos 'final'
public class AlunoService {

    // 3. Injeção de Dependência: O Service precisa do Repository
    private final AlunoRepository alunoRepository;

    /**
     * Método para criar um novo aluno.
     * REGRA: Não permite CPF de responsável duplicado.
     */
    @Transactional // 4. Garante que a operação é atômica (ou salva tudo, ou nada)
    public Aluno criarAluno(Aluno aluno) {
        // 5. REGRA DE NEGÓCIO:
        // Busca no banco se já existe um aluno com este CPF
        Optional<Aluno> alunoExistente = alunoRepository.findByCpfResponsavel(aluno.getCpfResponsavel());

        if (alunoExistente.isPresent()) {
            // Se existir, lança nossa exceção customizada
            throw new RegraNegocioException("CPF de responsável já cadastrado.");
        }

        // Se a regra passar, salva o aluno no banco
        return alunoRepository.save(aluno);
    }

    /**
     * Busca todos os alunos.
     */
    @Transactional(readOnly = true) // Otimiza a transação para apenas leitura
    public List<Aluno> listarTodos() {
        return alunoRepository.findAll();
    }

    /**
     * Busca um aluno pelo ID.
     * REGRA: Lança exceção se não encontrar.
     */
    @Transactional(readOnly = true)
    public Aluno buscarPorId(Long id) {
        return alunoRepository.findById(id)
                .orElseThrow(() -> new RegraNegocioException("Aluno não encontrado com o ID: " + id));
    }

    /**
     * Atualiza os dados de um aluno existente.
     */
    @Transactional
    public Aluno atualizarAluno(Long id, Aluno alunoAtualizado) {
        // 1. Busca o aluno (o método buscarPorId já valida se ele existe)
        Aluno alunoExistente = buscarPorId(id);

        // 2. Atualiza os dados do objeto existente
        // (Não permitimos alterar CPF ou data de matrícula por aqui)
        alunoExistente.setNome(alunoAtualizado.getNome());
        alunoExistente.setDataNascimento(alunoAtualizado.getDataNascimento());
        alunoExistente.setNomeResponsavel(alunoAtualizado.getNomeResponsavel());
        alunoExistente.setTelefoneResponsavel(alunoAtualizado.getTelefoneResponsavel());
        alunoExistente.setEmailResponsavel(alunoAtualizado.getEmailResponsavel());
        alunoExistente.setAtivo(alunoAtualizado.getAtivo());

        // 3. Salva (o JPA entende que é um UPDATE, pois o 'alunoExistente' tem ID)
        return alunoRepository.save(alunoExistente);
    }

    /**
     * Desativa um aluno (Soft Delete).
     */
    @Transactional
    public void desativarAluno(Long id) {
        Aluno aluno = buscarPorId(id);
        aluno.setAtivo(false);
        alunoRepository.save(aluno);
    }
}