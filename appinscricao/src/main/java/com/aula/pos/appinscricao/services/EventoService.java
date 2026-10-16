package com.aula.pos.appinscricao.services;

import com.aula.pos.appinscricao.dto.EventoRequest;
import com.aula.pos.appinscricao.dto.EventoResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

public interface EventoService {
    Page<EventoResponse> findAll(String nome, String cidade, String estado, Pageable pageable);
    EventoResponse findById(UUID id);
    EventoResponse save(EventoRequest eventoRequest);
    EventoResponse update(UUID id, EventoRequest eventoRequest);
    void delete(UUID id);
}
