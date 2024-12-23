package com.vm.user.model.request;


import jakarta.validation.constraints.Email;
import lombok.*;
import jakarta.validation.constraints.NotBlank;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MaintenanceUserRequest {

    @NotBlank(message = "O campo email não pode ser vazio.")
    @Email(message = "O email deve estar em um formato valido.")
    private String email;

    @NotBlank(message = "O campo nome do usuario não pode ser vazio.")
    private String name;

    @NotBlank(message = "O campo senha do usuario não pode ser vazio.")
    private String password;
}
