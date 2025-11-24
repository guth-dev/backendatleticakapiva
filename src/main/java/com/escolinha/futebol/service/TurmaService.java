package com.escolinha.futebol.service;

import com.escolinha.futebol.dto.TurmaRequestDTO;
import com.escolinha.futebol.model.Turma;
import com.escolinha.futebol.model.Professor;
import com.escolinha.futebol.repository.ProfessorRepository;
import com.escolinha.futebol.repository.TurmaRepository;
import com.escolinha.futebol.service.exceptions.RegraNegocioException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TurmaService {

    private final TurmaRepository turmaRepository;
    private final ProfessorRepository professorRepository;

    @Transactional
    public Turma criarTurma(TurmaRequestDTO dto) {

        Professor professor = professorRepository.findById(dto.professorId())
                .orElseThrow(() ->
                        new RegraNegocioException("Professor com o ID " + dto.professorId() + " não existe."));

        Turma turma = new Turma();
        turma.setNome(dto.nome());
        turma.setFaixaEtariaMinima(dto.faixaEtariaMinima());
        turma.setFaixaEtariaMaxima(dto.faixaEtariaMaxima());
        turma.setLimiteAlunos(dto.limiteAlunos());
        turma.setProfessor(professor);

        return turmaRepository.save(turma);
    }

    @Transactional(readOnly = true)
    public List<Turma> listarTodas() {
        return turmaRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Turma buscarPorId(Long id) {
        return turmaRepository.findById(id)
                .orElseThrow(() -> new RegraNegocioException("Turma não encontrada com o ID: " + id));
    }

    @Transactional
    public void deletarTurma(Long id) {
        Turma turma = buscarPorId(id);

        boolean possuiMatriculasAtivas =
                turma.getMatriculas() != null &&
                        turma.getMatriculas().stream().anyMatch(m ->
                                m.getStatus() != null && m.getStatus().name().equals("ATIVA"));

        if (possuiMatriculasAtivas) {
            throw new RegraNegocioException("Não é possível excluir a turma pois há alunos matriculados.");
        }

        turmaRepository.deleteById(id);
    }
}
