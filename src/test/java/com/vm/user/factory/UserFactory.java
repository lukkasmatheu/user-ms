package com.vm.user.factory;

import com.vm.user.model.entity.UserEntity;
import com.vm.user.model.request.MaintenanceUserRequest;
import com.vm.user.model.response.UserResponse;

public class UserFactory {

    public static MaintenanceUserRequest buildValidRequest() {
        return MaintenanceUserRequest.builder()
                .email("lucasM@gmail.com")
                .name("Lucas Matheus")
                .password("Senha123")
                .build();
    }

    public static MaintenanceUserRequest buildInvalidRequest() {
        return MaintenanceUserRequest.builder()
                .email("lucasM@gmail.com")
                .name("Lucas Matheus")
                .build();
    }

    public static UserResponse buildValidResponse() {
        return UserResponse.builder()
                .email("lucasM@gmail.com")
                .name("Lucas Matheus")
                .userId("1")
                .build();
    }

    public static UserEntity buildValidEntity(){
        return UserEntity.builder()
                .email("lucasM@gmail.com")
                .id("1")
                .name("Lucas Matheus")
                .build();
    }

}
