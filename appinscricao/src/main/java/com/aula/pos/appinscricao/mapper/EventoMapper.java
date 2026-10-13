package com.aula.pos.appinscricao.mapper;

import com.aula.pos.appinscricao.dto.EventoRequest;
import com.aula.pos.appinscricao.dto.EventoResponse;
import com.aula.pos.appinscricao.model.Evento;
import org.springframework.stereotype.Component;

@Component
public class EventoMapper {
    public Evento toEntity(EventoRequest eventoRequest) {
        return new Evento(
                null,
                eventoRequest.nome().trim(),
                eventoRequest.dataHoraEvento(),
                eventoRequest.local().trim(),
                eventoRequest.cidade().trim(),
                eventoRequest.estado().trim(),
                eventoRequest.qtde()
        );
    }

    public void updateEntity(EventoRequest eventoRequest, Evento evento){
        evento.setNome(eventoRequest.nome().trim());
        evento.setDataHoraEvento(eventoRequest.dataHoraEvento());
        evento.setLocal(eventoRequest.local().trim());
        evento.setCidade(eventoRequest.cidade().trim());
        evento.setEstado(eventoRequest.estado().trim());
        evento.setQtde(eventoRequest.qtde());
    }

    public EventoResponse toResponse(Evento eventoResponse){
        return new EventoResponse(
                eventoResponse.getId(),
                eventoResponse.getNome(),
                eventoResponse.getDataHoraEvento(),
                eventoResponse.getLocal(),
                eventoResponse.getCidade(),
                eventoResponse.getEstado(),
                eventoResponse.getQtde()
        );
    }
}
