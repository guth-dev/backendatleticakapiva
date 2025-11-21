package com.escolinha.futebol.service;

import com.escolinha.futebol.model.Aula;
import com.escolinha.futebol.repository.AulaRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class AulaService {

    private final AulaRepository aulaRepository;

    // Injeção pelo construtor (mais moderno, não precisa de @Autowired)
    public AulaService(AulaRepository aulaRepository) {
        this.aulaRepository = aulaRepository;
    }

    public List<Aula> listar() {
        return aulaRepository.findAll();
    }

    public Aula criar(Aula aula) {
        return aulaRepository.save(aula);
    }

    public void deletar(Long id) {
        aulaRepository.deleteById(id);
    }

    public Optional<Aula> buscarPorId(Long id) {
        return aulaRepository.findById(id);
    }

    public Aula editar(Long id, Aula dados) {
        return aulaRepository.findById(id)
                .map(aula -> {
                    aula.setTitulo(dados.getTitulo());
                    aula.setData(dados.getData());
                    aula.setHora(dados.getHora());
                    return aulaRepository.save(aula);
                })
                .orElseThrow(() -> new RuntimeException("Aula não encontrada"));
    }
}
