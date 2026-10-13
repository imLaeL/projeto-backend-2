package com.aula.pos.appinscricao.dto;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.time.LocalDateTime;

public record EventoRequest(
        @NotBlank(message = "O nome é obrigatório")
        @Size(max = 150, message = "O nome deve ter entre 3 e 100 caracteres")
        String nome,

        @NotBlank(message = "A data e horário é obrigatório")
        @Future(message = "A data e horário deve ser no futuro")
        LocalDateTime dataHoraEvento,

        @NotBlank(message = "O local é obrigatório")
        @Size(max = 200, message = "O local deve ter no máximo 200 caracteres")
        String local,

        @NotBlank(message = "O cidade é obrigatório")
        @Size(max = 100, message = "O cidade deve ter no máximo 100 caracteres")
        String cidade,

        @NotBlank(message = "O estado é obrigatório")
        @Size(min = 2, max = 2, message = "O estado deve ter no máximo 2 caracteres")
        String estado,

        @NotBlank(message = "O quantidade é obrigatório")
        @Size(min = 1, message = "O quantidade deve ter no mínimo 1 caracteres")
        Integer qtde
) {
}
