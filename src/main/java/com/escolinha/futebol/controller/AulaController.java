package com.escolinha.futebol.controller;

import com.escolinha.futebol.model.Aula;
import com.escolinha.futebol.service.AulaService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/aulas")
@CrossOrigin(origins = "http://localhost:4200")
public class AulaController {

    private final AulaService aulaService;

    // Injeção via construtor (CORRETA)
    public AulaController(AulaService aulaService) {
        this.aulaService = aulaService;
    }

    @GetMapping
    public List<Aula> listar() {
        return aulaService.listar();
    }

    @PostMapping
    public Aula criar(@RequestBody Aula aula) {
        return aulaService.criar(aula);
    }

    @DeleteMapping("/{id}")
    public void deletar(@PathVariable Long id) {
        aulaService.deletar(id);
    }

    @PutMapping("/{id}")
    public Aula editar(@PathVariable Long id, @RequestBody Aula aula) {
        return aulaService.editar(id, aula);
    }
}
