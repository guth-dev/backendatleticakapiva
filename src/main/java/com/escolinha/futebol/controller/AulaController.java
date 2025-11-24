package com.escolinha.futebol.controller;

import com.escolinha.futebol.dto.AulaRequestDTO;
import com.escolinha.futebol.model.Aula;
import com.escolinha.futebol.service.AulaService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/aulas")
@CrossOrigin(origins = "http://localhost:4200")
public class AulaController {

    private final AulaService aulaService;

    public AulaController(AulaService aulaService) {
        this.aulaService = aulaService;
    }

    @GetMapping
    public List<Aula> listar() {
        return aulaService.listar();
    }

    @PostMapping
    public Aula criar(@RequestBody AulaRequestDTO dto) {
        Aula aula = new Aula();
        aula.setData(dto.data());          // agora usa data
        aula.setHoraInicio(dto.horaInicio());
        aula.setHoraFim(dto.horaFim());

        return aulaService.criar(aula, dto.turmaId());
    }

    @PutMapping("/{id}")
    public Aula editar(@PathVariable Long id, @RequestBody AulaRequestDTO dto) {
        Aula aula = new Aula();
        aula.setData(dto.data());          // agora usa data
        aula.setHoraInicio(dto.horaInicio());
        aula.setHoraFim(dto.horaFim());

        return aulaService.editar(id, aula, dto.turmaId());
    }

    @DeleteMapping("/{id}")
    public void deletar(@PathVariable Long id) {
        aulaService.deletar(id);
    }
}
