package com.escolinha.futebol.service;

import com.escolinha.futebol.model.Aluno;
import com.escolinha.futebol.model.Matricula;
import com.escolinha.futebol.model.enums.StatusMatricula;
import com.escolinha.futebol.repository.AlunoRepository;
import com.escolinha.futebol.repository.MatriculaRepository;
import com.escolinha.futebol.service.exceptions.RegraNegocioException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AlunoService {

    private final AlunoRepository alunoRepository;
    private final MatriculaRepository matriculaRepository; // Adicionado

    /**
     * Criar aluno com validação de CPF
     * + geração de código de matrícula (ANO + sequencial)
     * + criação automática de matrícula inicial
     */
    @Transactional
    public Aluno criarAluno(Aluno aluno) {

        // Verificar CPF duplicado
        Optional<Aluno> alunoExistente = alunoRepository.findByCpfResponsavel(aluno.getCpfResponsavel());
        if (alunoExistente.isPresent()) {
            throw new RegraNegocioException("CPF de responsável já cadastrado.");
        }

        // Gerar código do aluno (ANO + sequencial → 2025-0001)
        String codigoGerado = gerarCodigoAluno();
        aluno.setCodigoAluno(codigoGerado);

        // Salvar aluno
        Aluno alunoSalvo = alunoRepository.save(aluno);

        // Criar matrícula inicial automaticamente
        Matricula matricula = new Matricula();
        matricula.setAluno(alunoSalvo);
        matricula.setDataMatricula(LocalDate.now());
        matricula.setStatus(StatusMatricula.ATIVA);

        matriculaRepository.save(matricula); // Salva a matrícula

        return alunoSalvo;
    }

    @Transactional(readOnly = true)
    public List<Aluno> listarTodos() {
        return alunoRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Aluno buscarPorId(Long id) {
        return alunoRepository.findById(id)
                .orElseThrow(() -> new RegraNegocioException("Aluno não encontrado com o ID: " + id));
    }

    @Transactional
    public Aluno atualizarAluno(Long id, Aluno alunoAtualizado) {

        Aluno alunoExistente = buscarPorId(id);

        // Validação de CPF ao atualizar
        if (!alunoExistente.getCpfResponsavel().equals(alunoAtualizado.getCpfResponsavel())) {
            alunoRepository.findByCpfResponsavel(alunoAtualizado.getCpfResponsavel())
                    .ifPresent(outroAluno -> {
                        throw new RegraNegocioException("CPF já cadastrado para outro aluno.");
                    });
            alunoExistente.setCpfResponsavel(alunoAtualizado.getCpfResponsavel());
        }

        // Atualização normal dos dados
        alunoExistente.setNome(alunoAtualizado.getNome());
        alunoExistente.setDataNascimento(alunoAtualizado.getDataNascimento());
        alunoExistente.setNomeResponsavel(alunoAtualizado.getNomeResponsavel());
        alunoExistente.setTelefoneResponsavel(alunoAtualizado.getTelefoneResponsavel());
        alunoExistente.setEmailResponsavel(alunoAtualizado.getEmailResponsavel());
        alunoExistente.setAtivo(alunoAtualizado.getAtivo());

        return alunoRepository.save(alunoExistente);
    }

    @Transactional
    public void desativarAluno(Long id) {
        Aluno aluno = buscarPorId(id);
        aluno.setAtivo(false);
        alunoRepository.save(aluno);
    }

    /**
     * Gera código ANO + sequencial (2025-0001)
     */
    private String gerarCodigoAluno() {
        String ano = String.valueOf(LocalDate.now().getYear());

        // Buscar a última matrícula daquele ano
        String ultima = alunoRepository.buscarUltimaMatriculaDoAno(ano);

        int sequencial = 1;
        if (ultima != null) {
            String[] partes = ultima.split("-");
            sequencial = Integer.parseInt(partes[1]) + 1;
        }

        return ano + "-" + String.format("%04d", sequencial);
    }

    // ------------------------------
    // NOVO MÉTODO: Buscar aluno pelo CÓDIGO
    // ------------------------------
    @Transactional(readOnly = true)
    public Aluno buscarPorCodigo(String codigoAluno) {
        return alunoRepository.findByCodigoAluno(codigoAluno)
                .orElseThrow(() -> new RegraNegocioException("Aluno não encontrado ou código inválido"));
    }
}
