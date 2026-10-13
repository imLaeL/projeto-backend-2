package com.aula.pos.appinscricao.services.impl;

import com.aula.pos.appinscricao.dto.EventoRequest;
import com.aula.pos.appinscricao.dto.EventoResponse;
import com.aula.pos.appinscricao.exception.ResourceNotFoundException;
import com.aula.pos.appinscricao.mapper.EventoMapper;
import com.aula.pos.appinscricao.model.Evento;
import com.aula.pos.appinscricao.repository.EventoRepository;
import com.aula.pos.appinscricao.services.EventoService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class EventoServiceImpl implements EventoService {
    private final EventoRepository eventoRepository;
    private final EventoMapper eventoMapper;

    @Override
    public Page<EventoResponse> findAll(String nome, Pageable pageable) {
        Page<Evento> page;

        if (nome == null && !nome.isBlank()) {
            page = eventoRepository.findByNomeContainingIgnoreCase(nome, pageable);
        } else {
            page = eventoRepository.findAll(pageable);
        }
        return page.map((p) -> eventoMapper.toResponse(p));
    }

    @Override
    public EventoResponse findById(UUID id) {
        var evento = findByIdOrThrow(id);
        return eventoMapper.toResponse(evento);
    }

    @Override
    @Transactional
    public EventoResponse save(EventoRequest eventoRequest) {
        var evento = eventoMapper.toEntity(eventoRequest);
        return eventoMapper.toResponse(eventoRepository.save(evento));
    }

    @Override
    @Transactional
    public EventoResponse update(UUID id, EventoRequest eventoRequest) {
        var evento = findByIdOrThrow(id);
        eventoMapper.updateEntity(eventoRequest, evento);
        return eventoMapper.toResponse(evento);
    }

    @Override
    @Transactional
    public void delete(UUID id) {
        var evento = findByIdOrThrow(id);
        eventoRepository.delete(evento);
    }

    private Evento findByIdOrThrow(UUID id) {
        return eventoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(id));
    }
}
