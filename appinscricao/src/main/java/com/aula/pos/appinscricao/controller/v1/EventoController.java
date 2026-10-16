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
    public ResponseEntity<?> findById(@PathVariable UUID id){
        try {
            return ResponseEntity.ok(eventoService.findById(id));
        } catch (ApiException e) {
            return errorResponse(e);
        }
    }

    @PostMapping
    public ResponseEntity<?> save(@Valid @RequestBody EventoRequest eventoRequest){
        try {
            return ResponseEntity.status(HttpStatus.CREATED).body(eventoService.save(eventoRequest));
        } catch (ApiException e) {
            return errorResponse(e);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable UUID id, @Valid @RequestBody EventoRequest eventoRequest){
        try {
            return ResponseEntity.ok(eventoService.update(id, eventoRequest));
        } catch (ApiException e) {
            return errorResponse(e);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteById(@PathVariable UUID id){
        try {
            eventoService.delete(id);
            return ResponseEntity.noContent().build();
        } catch (ApiException e) {
            return errorResponse(e);
        }
    }

    private ResponseEntity<ErrorResponse> errorResponse(ApiException e) {
        return ResponseEntity.status(e.getStatus())
                .body(new ErrorResponse(LocalDateTime.now(), e.getStatus().value(), e.getMessage()));
    }
}
