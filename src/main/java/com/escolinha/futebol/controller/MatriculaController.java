package com.escolinha.futebol.controller;

import com.escolinha.futebol.dto.MatriculaMinDTO;
import com.escolinha.futebol.dto.MatriculaRequestDTO;
import com.escolinha.futebol.model.Matricula;
import com.escolinha.futebol.model.enums.StatusMatricula;
import com.escolinha.futebol.service.MatriculaService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/matriculas")
@RequiredArgsConstructor
public class MatriculaController {

    private final MatriculaService matriculaService;

    @PostMapping
    public ResponseEntity<MatriculaMinDTO> realizarMatricula(
            @RequestBody @Valid MatriculaRequestDTO requestDTO) {

        Matricula novaMatricula = matriculaService.realizarMatricula(
                requestDTO.alunoId(),
                requestDTO.turmaId()
        );

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(MatriculaMinDTO.fromEntity(novaMatricula));
    }

    @GetMapping
    public ResponseEntity<List<MatriculaMinDTO>> listarMatriculas(
            @RequestParam(required = false) Long alunoId,
            @RequestParam(required = false) Long turmaId) {

        List<Matricula> matriculas = matriculaService.listar(alunoId, turmaId);

        List<MatriculaMinDTO> respostaDTOs = matriculas.stream()
                .map(MatriculaMinDTO::fromEntity)
                .toList();

        return ResponseEntity.ok(respostaDTOs);
    }

    @PutMapping("/{id}/trancar")
    public ResponseEntity<MatriculaMinDTO> trancarMatricula(@PathVariable Long id) {
        Matricula matriculaTrancada = matriculaService.trancarMatricula(id);
        return ResponseEntity.ok(MatriculaMinDTO.fromEntity(matriculaTrancada));
    }

    // 🔥 NOVA ROTA: alterar status da matrícula
    @PutMapping("/{id}/status")
    public ResponseEntity<MatriculaMinDTO> alterarStatus(
            @PathVariable Long id,
            @RequestParam StatusMatricula status) {

        Matricula matriculaAtualizada = matriculaService.alterarStatus(id, status);
        return ResponseEntity.ok(MatriculaMinDTO.fromEntity(matriculaAtualizada));
    }
}
