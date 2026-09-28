package com.aula.pos.appinscricao.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record PessoaRequest(
        @NotBlank(message = "O nome é obrigatório")
        @Size(min = 3, max = 100, message = "O nome deve ter entre 3 e 100 caracteres")
        String nome,

        @NotBlank(message = "O CPF é obrigatório")
        @Size(max = 100, message = "CPF deve possuir no máximo 100 caracteres")
        String cpf,

        @NotBlank(message = "O email é obrigatório")
        @Size(max = 100, message = "email deve possuir no máximo 15 caracteres")
        String email,

        @NotBlank(message = "O telefone é obrigatório")
        @Size(max = 15, message = "telefone deve possuir no máximo 15 caracteres")
        String telefone
) {
}
