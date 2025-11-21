package com.escolinha.futebol.controller;

import com.escolinha.futebol.dto.TurmaRequestDTO;
import com.escolinha.futebol.dto.TurmaResponseDTO;
import com.escolinha.futebol.model.Turma;
import com.escolinha.futebol.service.MatriculaService;
import com.escolinha.futebol.service.TurmaService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/turmas")
@RequiredArgsConstructor
public class TurmaController {

    private final TurmaService turmaService;
    private final MatriculaService matriculaService;

    /**
     * POST - Criar turma com alunos já matriculados automaticamente
     */
    @PostMapping
    public ResponseEntity<TurmaResponseDTO> criarTurma(
            @RequestBody @Valid TurmaRequestDTO turmaDTO) {

        // 1) Criar turma sem depender do MatriculaService
        Turma turmaSalva = turmaService.criarTurma(turmaDTO);

        // 2) Matricular automaticamente os alunos, se houver
        if (turmaDTO.alunoIds() != null && !turmaDTO.alunoIds().isEmpty()) {
            turmaDTO.alunoIds().forEach(alunoId ->
                    matriculaService.realizarMatricula(alunoId, turmaSalva.getId()));
        }

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(TurmaResponseDTO.fromEntity(turmaSalva));
    }

    /**
     * GET - Listar todas as turmas
     */
    @GetMapping
    public ResponseEntity<List<TurmaResponseDTO>> listarTurmas() {
        List<TurmaResponseDTO> lista = turmaService.listarTodas().stream()
                .map(TurmaResponseDTO::fromEntity)
                .collect(Collectors.toList());
        return ResponseEntity.ok(lista);
    }

    /**
     * GET - Buscar turma por ID
     */
    @GetMapping("/{id}")
    public ResponseEntity<TurmaResponseDTO> buscarTurmaPorId(@PathVariable Long id) {
        Turma turma = turmaService.buscarPorId(id);
        return ResponseEntity.ok(TurmaResponseDTO.fromEntity(turma));
    }

    /**
     * DELETE - Excluir turma
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletarTurma(@PathVariable Long id) {
        turmaService.deletarTurma(id);
        return ResponseEntity.noContent().build();
    }
}
