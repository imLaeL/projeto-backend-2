package com.aula.pos.appinscricao.controller.v1;

import com.aula.pos.appinscricao.dto.ErrorResponse;
import com.aula.pos.appinscricao.dto.EventoRequest;
import com.aula.pos.appinscricao.dto.EventoResponse;
import com.aula.pos.appinscricao.exception.ApiException;
import com.aula.pos.appinscricao.services.EventoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/eventos")
@RequiredArgsConstructor
public class EventoController {

    private final EventoService eventoService;

    @GetMapping
    public Page<EventoResponse> findAll(
            @RequestParam(required = false) String nome,
            @RequestParam(required = false) String cidade,
            @RequestParam(required = false) String estado,
            @ParameterObject @PageableDefault(size = 10, sort = "dataHoraEvento", direction = Sort.Direction.ASC) Pageable pageable
    ){
        return eventoService.findAll(nome, cidade, estado, pageable);
    }

    @GetMapping("/{id}")
    public ResponseEntity<EventoResponse> findById(@PathVariable UUID id) {
        return ResponseEntity.ok(eventoService.findById(id));
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public EventoResponse save(@Valid @RequestBody EventoRequest request) {
        return eventoService.save(request);
    }

    @PutMapping("/{id}")
    public ResponseEntity<EventoResponse> update(
            @PathVariable UUID id,
            @Valid @RequestBody EventoRequest request
    ) {
        return ResponseEntity.ok(eventoService.update(id, request));
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteById(@PathVariable UUID id) {
        eventoService.delete(id);
    }
}
