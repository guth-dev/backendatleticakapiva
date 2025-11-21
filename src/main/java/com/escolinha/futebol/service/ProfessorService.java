package com.escolinha.futebol.service;

import com.escolinha.futebol.model.Professor;
import com.escolinha.futebol.repository.ProfessorRepository;
import com.escolinha.futebol.service.exceptions.RegraNegocioException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ProfessorService {

    private final ProfessorRepository professorRepository;

    /**
     * Cria um novo professor.
     * REGRA: CPF não pode ser duplicado.
     */
    @Transactional
    public Professor criarProfessor(Professor professor) {
        // 1. Precisamos adicionar o método 'findByCpf' no ProfessorRepository
        Optional<Professor> professorExistente = professorRepository.findByCpf(professor.getCpf());

        if (professorExistente.isPresent()) {
            throw new RegraNegocioException("CPF de professor já cadastrado.");
        }

        return professorRepository.save(professor);
    }

    @Transactional(readOnly = true)
    public List<Professor> listarTodos() {
        return professorRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Professor buscarPorId(Long id) {
        return professorRepository.findById(id)
                .orElseThrow(() -> new RegraNegocioException("Professor não encontrado com o ID: " + id));
    }

    /**
     * Atualiza um professor.
     */
    @Transactional
    public Professor atualizarProfessor(Long id, Professor professorAtualizado) {
        Professor professorExistente = buscarPorId(id); // Valida se existe

        // Atualiza os campos permitidos
        professorExistente.setNome(professorAtualizado.getNome());

        // Não alteramos CPF ou data de contratação por aqui

        return professorRepository.save(professorExistente);
    }

    /**
     * Deleta um professor.
     * REGRA: Não permite deletar se houver turmas vinculadas.
     */
    @Transactional
    public void deletarProfessor(Long id) {
        Professor professor = buscarPorId(id);

        // O 'turmas' é o List<Turma> que mapeamos na Entidade Professor
        if (professor.getTurmas() != null && !professor.getTurmas().isEmpty()) {
            throw new RegraNegocioException("Não é possível excluir o professor, pois ele está associado a uma ou mais turmas.");
        }

        professorRepository.deleteById(id);
    }
}