package br.com.mesdra.webfluxproject.model.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record UserRequest(
        @NotBlank(message = "Nome não pode ser vazio")
        @Size(min = 3, max = 50, message = "Nome deve conter entre 3 e 50 caracteres")

        String name,
        @NotBlank(message = "Email não pode ser vazio")
        @Email(message = "Email Invalido")
        String email,
        @NotBlank(message = "Email não pode ser vazio")
        @Size(min = 3, max = 20, message = "Senha deve conter de 3 a 20 caracteres")
        String password
) {
}
