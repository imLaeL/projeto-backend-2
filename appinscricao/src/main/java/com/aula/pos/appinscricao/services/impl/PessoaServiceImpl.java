package com.aula.pos.appinscricao.services.impl;

import com.aula.pos.appinscricao.dto.PessoaRequest;
import com.aula.pos.appinscricao.dto.PessoaResponse;
import com.aula.pos.appinscricao.exception.ResourceNotFoundException;
import com.aula.pos.appinscricao.mapper.PessoaMapper;
import com.aula.pos.appinscricao.model.Pessoa;
import com.aula.pos.appinscricao.repository.PessoaRepository;
import com.aula.pos.appinscricao.services.PessoaService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PessoaServiceImpl implements PessoaService {
    private final PessoaRepository pessoaRepository;
    private final PessoaMapper mapper;

    @Override
    public Page<PessoaResponse> findAll(String nome, Pageable pageable) {
        Page<Pessoa> page;

        if(nome != null && !nome.isBlank()) {
            page = pessoaRepository.findByNomeContainingIgnoreCase(nome, pageable);
        } else {
            page = pessoaRepository.findAll(pageable);
        }
        return page.map((p) -> mapper.toResponse(p));
    }

    @Override
    public PessoaResponse findById(UUID id) {
        var pessoa = findByIdOrThrow(id);
        return mapper.toResponse(pessoa);
    }

    @Override
    @Transactional
    public PessoaResponse save(PessoaRequest pessoaRequest) {
        var pessoa = mapper.toEntity(pessoaRequest);
        return mapper.toResponse(pessoaRepository.save(pessoa));
    }

    @Override
    @Transactional
    public PessoaResponse update(UUID id, PessoaRequest pessoaRequest) {
        var pessoa = findByIdOrThrow(id);
        mapper.updateEntity(pessoaRequest, pessoa);
        return mapper.toResponse(pessoa);
    }

    @Override
    @Transactional
    public void delete(UUID id) {
        var pessoa = findByIdOrThrow(id);
        pessoaRepository.delete(pessoa);
    }
    
    private Pessoa findByIdOrThrow(UUID id) {
        return pessoaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(id));
    }
}
