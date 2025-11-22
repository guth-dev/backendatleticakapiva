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

    @Transactional
    public Matricula realizarMatricula(Long alunoId, Long turmaId) {

        Aluno aluno = alunoService.buscarPorId(alunoId);
        Turma turma = turmaService.buscarPorId(turmaId);

        if (!aluno.getAtivo()) {
            throw new RegraNegocioException("O aluno está inativo e não pode ser matriculado.");
        }

        Optional<Matricula> existente = matriculaRepository
                .findByAlunoIdAndTurmaIdAndStatus(alunoId, turmaId, StatusMatricula.ATIVA);

        if (existente.isPresent()) {
            throw new RegraNegocioException("O aluno já possui matrícula ativa nesta turma.");
        }

        if (turma.getLimiteAlunos() != null && turma.getLimiteAlunos() > 0) {
            long ativos = matriculaRepository.countByTurmaIdAndStatus(turmaId, StatusMatricula.ATIVA);

            if (ativos >= turma.getLimiteAlunos()) {
                throw new RegraNegocioException("A turma está lotada.");
            }
        }

        int idade = Period.between(aluno.getDataNascimento(), LocalDate.now()).getYears();

        if (turma.getFaixaEtariaMinima() != null && idade < turma.getFaixaEtariaMinima()) {
            throw new RegraNegocioException("O aluno não atende a idade mínima da turma.");
        }

        if (turma.getFaixaEtariaMaxima() != null && idade > turma.getFaixaEtariaMaxima()) {
            throw new RegraNegocioException("O aluno excede a idade máxima da turma.");
        }

        Matricula nova = new Matricula();
        nova.setAluno(aluno);
        nova.setTurma(turma);
        nova.setDataMatricula(LocalDate.now());
        nova.setStatus(StatusMatricula.ATIVA);

        return matriculaRepository.save(nova);
    }

    @Transactional
    public Matricula trancarMatricula(Long matriculaId) {
        Matricula matricula = matriculaRepository.findById(matriculaId)
                .orElseThrow(() -> new RegraNegocioException("Matrícula não encontrada."));

        matricula.setStatus(StatusMatricula.TRANCADA);
        matricula.setDataFim(LocalDate.now());

        return matriculaRepository.save(matricula);
    }

    @Transactional(readOnly = true)
    public List<Matricula> listar(Long alunoId, Long turmaId) {

        if (alunoId != null) {
            return matriculaRepository.findByAlunoId(alunoId);
        }

        if (turmaId != null) {
            return matriculaRepository.findByTurmaId(turmaId);
        }

        return matriculaRepository.findAll();
    }
}
