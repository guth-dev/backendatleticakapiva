package com.escolinha.futebol.service;

import com.escolinha.futebol.model.Aula;
import com.escolinha.futebol.model.Turma;
import com.escolinha.futebol.repository.AulaRepository;
import com.escolinha.futebol.repository.TurmaRepository;
import com.escolinha.futebol.service.exceptions.RegraNegocioException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AulaService {

    private final AulaRepository aulaRepository;
    private final TurmaRepository turmaRepository;

    public List<Aula> listar() {
        return aulaRepository.findAll();
    }

    public Aula criar(Aula dados, Long turmaId) {

        Turma turma = turmaRepository.findById(turmaId)
                .orElseThrow(() -> new RegraNegocioException("Turma não encontrada."));

        Aula aula = new Aula();
        aula.setData(dados.getData());
        aula.setHoraInicio(dados.getHoraInicio());
        aula.setHoraFim(dados.getHoraFim());
        aula.setTurma(turma);

        // título no formato:
        // Sub - 16
        // Rogério
        // 18h–19h
        String titulo =
                turma.getNome() + "\n" +
                        turma.getProfessor().getNome() + "\n" +
                        dados.getHoraInicio().substring(0, 2) + "h–" +
                        dados.getHoraFim().substring(0, 2) + "h";

        aula.setTitulo(titulo);

        return aulaRepository.save(aula);
    }

    public Aula editar(Long id, Aula dados, Long turmaId) {

        Aula aula = aulaRepository.findById(id)
                .orElseThrow(() -> new RegraNegocioException("Aula não encontrada."));

        Turma turma = turmaRepository.findById(turmaId)
                .orElseThrow(() -> new RegraNegocioException("Turma não encontrada."));

        aula.setData(dados.getData());
        aula.setHoraInicio(dados.getHoraInicio());
        aula.setHoraFim(dados.getHoraFim());
        aula.setTurma(turma);

        String titulo =
                turma.getNome() + "\n" +
                        turma.getProfessor().getNome() + "\n" +
                        dados.getHoraInicio().substring(0, 2) + "h–" +
                        dados.getHoraFim().substring(0, 2) + "h";

        aula.setTitulo(titulo);

        return aulaRepository.save(aula);
    }

    public void deletar(Long id) {
        aulaRepository.deleteById(id);
    }
}
