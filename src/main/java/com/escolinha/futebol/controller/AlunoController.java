package com.escolinha.futebol.controller;

import com.escolinha.futebol.dto.AlunoRequestDTO;
import com.escolinha.futebol.dto.AlunoResponseDTO;
import com.escolinha.futebol.dto.AlunoPainelResponseDTO;
import com.escolinha.futebol.model.Aluno;
import com.escolinha.futebol.model.Matricula;
import com.escolinha.futebol.service.AlunoService;
import com.escolinha.futebol.service.MatriculaService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/alunos")
@RequiredArgsConstructor
public class AlunoController {

    private final AlunoService alunoService;
    private final MatriculaService matriculaService;

    private Aluno convertToEntity(AlunoRequestDTO dto) {
        Aluno aluno = new Aluno();
        aluno.setNome(dto.nome());
        aluno.setDataNascimento(dto.dataNascimento());
        aluno.setNomeResponsavel(dto.nomeResponsavel());
        aluno.setCpfResponsavel(dto.cpfResponsavel());
        aluno.setEmailResponsavel(dto.emailResponsavel());
        aluno.setTelefoneResponsavel(dto.telefoneResponsavel());
        return aluno;
    }

    @PostMapping
    public ResponseEntity<AlunoResponseDTO> criarAluno(@RequestBody @Valid AlunoRequestDTO alunoDTO) {
        Aluno alunoParaSalvar = convertToEntity(alunoDTO);
        Aluno alunoSalvo = alunoService.criarAluno(alunoParaSalvar);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(AlunoResponseDTO.fromEntity(alunoSalvo));
    }

    @GetMapping
    public ResponseEntity<List<AlunoResponseDTO>> listarAlunos() {
        List<AlunoResponseDTO> respostaDTOs = alunoService.listarTodos().stream()
                .map(AlunoResponseDTO::fromEntity)
                .collect(Collectors.toList());
        return ResponseEntity.ok(respostaDTOs);
    }

    @GetMapping("/{id}")
    public ResponseEntity<AlunoResponseDTO> buscarAlunoPorId(@PathVariable Long id) {
        Aluno aluno = alunoService.buscarPorId(id);
        return ResponseEntity.ok(AlunoResponseDTO.fromEntity(aluno));
    }

    @PutMapping("/{id}")
    public ResponseEntity<AlunoResponseDTO> atualizarAluno(
            @PathVariable Long id,
            @RequestBody @Valid AlunoRequestDTO alunoDTO
    ) {
        Aluno alunoParaAtualizar = convertToEntity(alunoDTO);
        Aluno alunoAtualizado = alunoService.atualizarAluno(id, alunoParaAtualizar);
        return ResponseEntity.ok(AlunoResponseDTO.fromEntity(alunoAtualizado));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> desativarAluno(@PathVariable Long id) {
        alunoService.desativarAluno(id);
        return ResponseEntity.noContent().build();
    }

    // --------------------------
    //    PAINEL DO ALUNO
    // --------------------------
    @GetMapping("/{id}/painel")
    public ResponseEntity<AlunoPainelResponseDTO> buscarPainelAluno(@PathVariable Long id) {

        // 1. Busca o aluno
        Aluno aluno = alunoService.buscarPorId(id);

        // 2. Busca a matrícula ativa
        Matricula matriculaAtiva = matriculaService.buscarMatriculaAtivaPorAluno(id);

        // 3. Converte para DTO
        AlunoPainelResponseDTO response =
                AlunoPainelResponseDTO.fromEntity(aluno, matriculaAtiva);

        return ResponseEntity.ok(response);
    }

    // --------------------------
    //    NOVO ENDPOINT: Buscar pelo CÓDIGO
    // --------------------------
    @GetMapping("/codigo/{codigo}")
    public ResponseEntity<AlunoResponseDTO> buscarAlunoPorCodigo(@PathVariable String codigo) {
        Aluno aluno = alunoService.buscarPorCodigo(codigo);
        return ResponseEntity.ok(AlunoResponseDTO.fromEntity(aluno));
    }
}
