package com.escolinha.futebol.controller;

import com.escolinha.futebol.dto.ProfessorRequestDTO;
import com.escolinha.futebol.dto.ProfessorResponseDTO;
import com.escolinha.futebol.model.Professor;
import com.escolinha.futebol.service.ProfessorService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/professores")
@RequiredArgsConstructor
public class ProfessorController {

    private final ProfessorService professorService;

    // ======================
    // POST – CRIAR PROFESSOR
    // ======================
    @PostMapping
    public ResponseEntity<ProfessorResponseDTO> criarProfessor(
            @RequestBody @Valid ProfessorRequestDTO dto) {

        Professor professor = new Professor();
        professor.setNome(dto.nome());

        // 🔥 Remove máscara do CPF antes de salvar
        String cpfLimpo = dto.cpf().replaceAll("\\D", "");
        professor.setCpf(cpfLimpo);

        professor.setDataContratacao(LocalDate.now());

        Professor salvo = professorService.criarProfessor(professor);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ProfessorResponseDTO.fromEntity(salvo));
    }

    // ==========================
    // GET – LISTAR PROFESSORES
    // ==========================
    @GetMapping
    public ResponseEntity<List<ProfessorResponseDTO>> listarProfessores() {
        List<ProfessorResponseDTO> lista = professorService.listarTodos().stream()
                .map(ProfessorResponseDTO::fromEntity)
                .collect(Collectors.toList());

        return ResponseEntity.ok(lista);
    }

    // =============================
    // GET – BUSCAR PROFESSOR POR ID
    // =============================
    @GetMapping("/{id}")
    public ResponseEntity<ProfessorResponseDTO> buscarPorId(@PathVariable Long id) {
        Professor professor = professorService.buscarPorId(id);
        return ResponseEntity.ok(ProfessorResponseDTO.fromEntity(professor));
    }

    // ============================
    // PUT – ATUALIZAR PROFESSOR
    // ============================
    @PutMapping("/{id}")
    public ResponseEntity<ProfessorResponseDTO> atualizar(
            @PathVariable Long id,
            @RequestBody @Valid ProfessorRequestDTO dto) {

        Professor dadosAtualizados = new Professor();
        dadosAtualizados.setNome(dto.nome());

        // 🔥 Remove máscara se quiser permitir editar CPF também
        String cpfLimpo = dto.cpf().replaceAll("\\D", "");
        dadosAtualizados.setCpf(cpfLimpo);

        Professor salvo = professorService.atualizarProfessor(id, dadosAtualizados);

        return ResponseEntity.ok(ProfessorResponseDTO.fromEntity(salvo));
    }

    // ============================
    // DELETE – DELETAR PROFESSOR
    // ============================
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        professorService.deletarProfessor(id);
        return ResponseEntity.noContent().build();
    }
}
