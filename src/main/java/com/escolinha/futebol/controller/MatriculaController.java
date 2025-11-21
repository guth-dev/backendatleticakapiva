package com.escolinha.futebol.controller;

import com.escolinha.futebol.dto.MatriculaRequestDTO;
import com.escolinha.futebol.dto.MatriculaResponseDTO;
import com.escolinha.futebol.model.Matricula;
import com.escolinha.futebol.service.MatriculaService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/matriculas")
@RequiredArgsConstructor
public class MatriculaController {

    private final MatriculaService matriculaService;

    /**
     * Endpoint: POST /api/matriculas
     * Realiza uma nova matrícula NORMAL (fluxo principal).
     */
    @PostMapping
    public ResponseEntity<MatriculaResponseDTO> realizarMatricula(
            @RequestBody @Valid MatriculaRequestDTO requestDTO) {

        Matricula novaMatricula = matriculaService.realizarMatricula(
                requestDTO.alunoId(),
                requestDTO.turmaId()
        );

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(MatriculaResponseDTO.fromEntity(novaMatricula));
    }

    /**
     * Endpoint: POST /api/matriculas/automatico
     * Usado automaticamente ao criar turmas — NÃO lança erro por duplicidade.
     */
    @PostMapping("/automatico")
    public ResponseEntity<MatriculaResponseDTO> matricularAutomatico(
            @RequestBody @Valid MatriculaRequestDTO requestDTO) {

        Matricula matricula = matriculaService.matricularAutomatico(
                requestDTO.alunoId(),
                requestDTO.turmaId()
        );

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(MatriculaResponseDTO.fromEntity(matricula));
    }

    /**
     * Endpoint: GET /api/matriculas
     * Lista matrículas com filtros opcionais por aluno ou turma.
     */
    @GetMapping
    public ResponseEntity<List<MatriculaResponseDTO>> listarMatriculas(
            @RequestParam(required = false) Long alunoId,
            @RequestParam(required = false) Long turmaId
    ) {
        List<Matricula> matriculas;

        if (alunoId != null) {
            matriculas = matriculaService.listarPorAluno(alunoId);
        } else if (turmaId != null) {
            matriculas = matriculaService.listarPorTurma(turmaId);
        } else {
            matriculas = matriculaService.listarTodas();
        }

        List<MatriculaResponseDTO> resposta = matriculas.stream()
                .map(MatriculaResponseDTO::fromEntity)
                .collect(Collectors.toList());

        return ResponseEntity.ok(resposta);
    }

    /**
     * Endpoint: PUT /api/matriculas/{id}/trancar
     * Tranca uma matrícula (status = TRANCADA).
     */
    @PutMapping("/{id}/trancar")
    public ResponseEntity<MatriculaResponseDTO> trancarMatricula(@PathVariable Long id) {
        Matricula matriculaTrancada = matriculaService.trancarMatricula(id);

        return ResponseEntity.ok(MatriculaResponseDTO.fromEntity(matriculaTrancada));
    }
}
