package com.aula.pos.appinscricao.services;

import com.aula.pos.appinscricao.dto.EventoRequest;
import com.aula.pos.appinscricao.dto.EventoResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

public interface EventoService {
    Page<EventoResponse> findAll(String nome, Pageable pageable);
    EventoResponse findById(UUID id);
    EventoResponse save(EventoRequest evento);
    EventoResponse update(UUID id, EventoRequest evento);
    void delete(UUID id);
}
