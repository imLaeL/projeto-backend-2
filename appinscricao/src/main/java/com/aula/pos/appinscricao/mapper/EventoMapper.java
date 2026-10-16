package com.aula.pos.appinscricao.mapper;

import com.aula.pos.appinscricao.dto.EventoRequest;
import com.aula.pos.appinscricao.dto.EventoResponse;
import com.aula.pos.appinscricao.model.Evento;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

@Component
public class EventoMapper {
    public Evento toEntity(EventoRequest eventoRequest) {
        return new Evento(
                null,
                eventoRequest.nome().trim(),
                // DATETIME column has no fractional seconds; truncate so duplicate check matches what is stored
                eventoRequest.dataHoraEvento().truncatedTo(ChronoUnit.SECONDS),
                eventoRequest.local().trim(),
                eventoRequest.cidade().trim(),
                eventoRequest.estado().trim().toUpperCase(),
                eventoRequest.qtde()
        );
    }

    public void updateEntity(EventoRequest eventoRequest, Evento evento){
        evento.setNome(eventoRequest.nome().trim());
        evento.setDataHoraEvento(eventoRequest.dataHoraEvento().truncatedTo(ChronoUnit.SECONDS));
        evento.setLocal(eventoRequest.local().trim());
        evento.setCidade(eventoRequest.cidade().trim());
        evento.setEstado(eventoRequest.estado().trim().toUpperCase());
        evento.setQtde(eventoRequest.qtde());
    }

    public EventoResponse toResponse(Evento evento){
        return new EventoResponse(
                evento.getId(),
                evento.getNome(),
                evento.getDataHoraEvento(),
                evento.getLocal(),
                evento.getCidade(),
                evento.getEstado(),
                evento.getQtde(),
                // Calendar days, so an event 10 days ahead is always 10 regardless of the hour
                ChronoUnit.DAYS.between(LocalDate.now(), evento.getDataHoraEvento().toLocalDate())
        );
    }
}
