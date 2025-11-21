package com.escolinha.futebol.service;

import com.escolinha.futebol.model.Aluno;
import com.escolinha.futebol.model.Matricula;
import com.escolinha.futebol.model.Turma;
import com.escolinha.futebol.model.enums.StatusMatricula;
import com.escolinha.futebol.repository.MatriculaRepository;
import com.escolinha.futebol.service.exceptions.RegraNegocioException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.Period;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MatriculaService {

    private final MatriculaRepository matriculaRepository;
    private final AlunoService alunoService;
    private final TurmaService turmaService;

    /**
     * Método usado pela TurmaController ao criar uma turma com alunos já inclusos.
     */
    @Transactional
    public Matricula matricularAutomatico(Long alunoId, Long turmaId) {
        return realizarMatricula(alunoId, turmaId);
    }

    @Transactional
    public Matricula realizarMatricula(Long alunoId, Long turmaId) {

        Aluno aluno = alunoService.buscarPorId(alunoId);
        Turma turma = turmaService.buscarPorId(turmaId);

        if (!aluno.getAtivo()) {
            throw new RegraNegocioException("O aluno " + aluno.getNome() + " está inativo e não pode ser matriculado.");
        }

        Optional<Matricula> matriculaExistente = matriculaRepository
                .findByAlunoIdAndTurmaIdAndStatus(alunoId, turmaId, StatusMatricula.ATIVA);

        if (matriculaExistente.isPresent()) {
            throw new RegraNegocioException("O aluno já possui uma matrícula ativa nesta turma.");
        }

        if (turma.getLimiteAlunos() != null && turma.getLimiteAlunos() > 0) {
            Long matriculasAtivas = matriculaRepository.countByTurmaIdAndStatus(turmaId, StatusMatricula.ATIVA);

            if (matriculasAtivas >= turma.getLimiteAlunos()) {
                throw new RegraNegocioException("A turma " + turma.getNome() + " está lotada.");
            }
        }

        int idadeAluno = Period.between(aluno.getDataNascimento(), LocalDate.now()).getYears();

        if (turma.getFaixaEtariaMinima() != null && idadeAluno < turma.getFaixaEtariaMinima()) {
            throw new RegraNegocioException("O aluno (idade " + idadeAluno + ") não atende a idade mínima da turma (" + turma.getFaixaEtariaMinima() + ").");
        }

        if (turma.getFaixaEtariaMaxima() != null && idadeAluno > turma.getFaixaEtariaMaxima()) {
            throw new RegraNegocioException("O aluno (idade " + idadeAluno + ") excede a idade máxima da turma (" + turma.getFaixaEtariaMaxima() + ").");
        }

        Matricula novaMatricula = new Matricula();
        novaMatricula.setAluno(aluno);
        novaMatricula.setTurma(turma);
        novaMatricula.setDataMatricula(LocalDate.now());
        novaMatricula.setStatus(StatusMatricula.ATIVA);

        return matriculaRepository.save(novaMatricula);
    }

    @Transactional
    public Matricula trancarMatricula(Long matriculaId) {
        Matricula matricula = matriculaRepository.findById(matriculaId)
                .orElseThrow(() -> new RegraNegocioException("Matrícula não encontrada com o ID: " + matriculaId));

        matricula.setStatus(StatusMatricula.TRANCADA);
        return matriculaRepository.save(matricula);
    }

    @Transactional(readOnly = true)
    public List<Matricula> listarTodas() {
        return matriculaRepository.findAll();
    }

    @Transactional(readOnly = true)
    public List<Matricula> listarPorAluno(Long alunoId) {
        alunoService.buscarPorId(alunoId);
        return matriculaRepository.findByAlunoId(alunoId);
    }

    @Transactional(readOnly = true)
    public List<Matricula> listarPorTurma(Long turmaId) {
        turmaService.buscarPorId(turmaId);
        return matriculaRepository.findByTurmaId(turmaId);
    }
}
