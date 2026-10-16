package com.aula.pos.appinscricao.services.impl;

import com.aula.pos.appinscricao.dto.EventoRequest;
import com.aula.pos.appinscricao.dto.EventoResponse;
import com.aula.pos.appinscricao.exception.BusinessException;
import com.aula.pos.appinscricao.exception.ConflictException;
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

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class EventoServiceImpl implements EventoService {
    private static final String EVENTO_DUPLICADO = "Já existe um evento com o mesmo nome, data/hora e cidade";

    private final EventoRepository eventoRepository;
    private final EventoMapper eventoMapper;

    @Override
    public Page<EventoResponse> findAll(String nome, String cidade, String estado, Pageable pageable) {
        return findPage(nome, cidade, estado, pageable).map(eventoMapper::toResponse);
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
        if (eventoRepository.existsByNomeIgnoreCaseAndDataHoraEventoAndCidadeIgnoreCase(
                evento.getNome(), evento.getDataHoraEvento(), evento.getCidade())) {
            throw new ConflictException(EVENTO_DUPLICADO);
        }
        return eventoMapper.toResponse(eventoRepository.save(evento));
    }

    @Override
    @Transactional
    public EventoResponse update(UUID id, EventoRequest eventoRequest) {
        var evento = findByIdOrThrow(id);
        var dadosNovos = eventoMapper.toEntity(eventoRequest);
        if (eventoRepository.existsByNomeIgnoreCaseAndDataHoraEventoAndCidadeIgnoreCaseAndIdNot(
                dadosNovos.getNome(), dadosNovos.getDataHoraEvento(), dadosNovos.getCidade(), id)) {
            throw new ConflictException(EVENTO_DUPLICADO);
        }
        eventoMapper.updateEntity(eventoRequest, evento);
        return eventoMapper.toResponse(evento);
    }

    @Override
    @Transactional
    public void delete(UUID id) {
        var evento = findByIdOrThrow(id);
        if (evento.getDataHoraEvento().isBefore(LocalDateTime.now())) {
            throw new BusinessException("Não é possível excluir um evento já realizado");
        }
        eventoRepository.delete(evento);
    }

    // Filter priority: nome -> cidade -> estado
    private Page<Evento> findPage(String nome, String cidade, String estado, Pageable pageable) {
        if (nome != null && !nome.isBlank()) {
            return eventoRepository.findByNomeContainingIgnoreCase(nome.trim(), pageable);
        }
        if (cidade != null && !cidade.isBlank()) {
            return eventoRepository.findByCidadeContainingIgnoreCase(cidade.trim(), pageable);
        }
        if (estado != null && !estado.isBlank()) {
            return eventoRepository.findByEstadoIgnoreCase(estado.trim(), pageable);
        }
        return eventoRepository.findAll(pageable);
    }

    private Evento findByIdOrThrow(UUID id) {
        return eventoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(id));
    }
}
