package com.aula.pos.appinscricao.mapper;

import com.aula.pos.appinscricao.dto.PessoaRequest;
import com.aula.pos.appinscricao.dto.PessoaResponse;
import com.aula.pos.appinscricao.model.Pessoa;
import org.springframework.stereotype.Component;

@Component
public class PessoaMapper {
    public Pessoa toEntity(PessoaRequest pessoaRequest) {
      return new Pessoa(
              null,
              pessoaRequest.nome().trim(),
              pessoaRequest.cpf().trim(),
              pessoaRequest.email().trim(),
              pessoaRequest.telefone().trim()
      );
    }

    public void updateEntity(PessoaRequest pessoaRequest, Pessoa pessoa) {
        pessoa.setNome(pessoaRequest.nome().trim());
        pessoa.setCpf(pessoaRequest.cpf().trim());
        pessoa.setEmail(pessoaRequest.email().trim());
        pessoa.setTelefone(pessoaRequest.telefone().trim());
    }

    public PessoaResponse toResponse(Pessoa pessoaResponse){
        return new PessoaResponse(
                pessoaResponse.getId(),
                pessoaResponse.getNome(),
                pessoaResponse.getCpf(),
                pessoaResponse.getEmail(),
                pessoaResponse.getTelefone()
        );
    }
}

