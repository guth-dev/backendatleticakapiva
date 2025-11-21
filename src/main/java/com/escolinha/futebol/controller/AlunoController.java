package com.escolinha.futebol.controller;

import com.escolinha.futebol.dto.AlunoRequestDTO;
import com.escolinha.futebol.dto.AlunoResponseDTO;
import com.escolinha.futebol.model.Aluno;
import com.escolinha.futebol.service.AlunoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController // 1. Diz ao Spring que esta classe é um Controller REST (lida com JSON)
@RequestMapping("/api/alunos") // 2. Define a URL base para todos os métodos nesta classe
@RequiredArgsConstructor // 3. (Lombok) Injeta o AlunoService
public class AlunoController {

    private final AlunoService alunoService;

    // --- MAPEAMENTO (DTO <-> Entidade) ---
    // Em um projeto real, isso ficaria numa classe "Mapper"
    // Mas para aprender, vamos fazer aqui.
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
    // (A conversão para ResponseDTO já está no próprio DTO)

    // --- ENDPOINTS ---

    /**
     * Endpoint: POST /api/alunos
     * Cria um novo aluno.
     */
    @PostMapping
    public ResponseEntity<AlunoResponseDTO> criarAluno(@RequestBody @Valid AlunoRequestDTO alunoDTO) {
        // 1. Converte o DTO (requisição) para Entidade
        Aluno alunoParaSalvar = convertToEntity(alunoDTO);

        // 2. Chama o Service (que tem a regra de negócio)
        Aluno alunoSalvo = alunoService.criarAluno(alunoParaSalvar);

        // 3. Converte a Entidade (salva) para DTO (resposta)
        AlunoResponseDTO respostaDTO = AlunoResponseDTO.fromEntity(alunoSalvo);

        // 4. Retorna HTTP 201 Created (Sucesso na criação)
        return ResponseEntity.status(HttpStatus.CREATED).body(respostaDTO);
    }

    /**
     * Endpoint: GET /api/alunos
     * Lista todos os alunos.
     */
    @GetMapping
    public ResponseEntity<List<AlunoResponseDTO>> listarAlunos() {
        // 1. Chama o Service
        List<Aluno> alunos = alunoService.listarTodos();

        // 2. Converte a Lista de Entidades para Lista de DTOs
        List<AlunoResponseDTO> respostaDTOs = alunos.stream()
                .map(AlunoResponseDTO::fromEntity) // (Chama o método estático)
                .collect(Collectors.toList());

        // 3. Retorna HTTP 200 OK
        return ResponseEntity.ok(respostaDTOs);
    }

    /**
     * Endpoint: GET /api/alunos/{id}
     * Busca um aluno pelo ID.
     */
    @GetMapping("/{id}")
    public ResponseEntity<AlunoResponseDTO> buscarAlunoPorId(@PathVariable Long id) {
        // 1. Chama o Service (que já lida com "não encontrado")
        Aluno aluno = alunoService.buscarPorId(id);

        // 2. Converte e retorna
        return ResponseEntity.ok(AlunoResponseDTO.fromEntity(aluno));
    }

    /**
     * Endpoint: PUT /api/alunos/{id}
     * Atualiza um aluno.
     */
    @PutMapping("/{id}")
    public ResponseEntity<AlunoResponseDTO> atualizarAluno(@PathVariable Long id, @RequestBody @Valid AlunoRequestDTO alunoDTO) {
        // 1. Converte DTO para Entidade
        Aluno alunoParaAtualizar = convertToEntity(alunoDTO);

        // 2. Chama o Service
        Aluno alunoAtualizado = alunoService.atualizarAluno(id, alunoParaAtualizar);

        // 3. Converte e retorna
        return ResponseEntity.ok(AlunoResponseDTO.fromEntity(alunoAtualizado));
    }

    /**
     * Endpoint: DELETE /api/alunos/{id}
     * Desativa um aluno (Soft Delete).
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> desativarAluno(@PathVariable Long id) {
        alunoService.desativarAluno(id);
        // 1. Retorna HTTP 204 No Content (Sucesso, sem corpo de resposta)
        return ResponseEntity.noContent().build();
    }
}