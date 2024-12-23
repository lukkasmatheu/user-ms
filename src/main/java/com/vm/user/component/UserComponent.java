package com.vm.user.component;

import com.vm.user.model.entity.UserEntity;
import com.vm.user.model.request.MaintenanceUserRequest;
import com.vm.user.model.response.UserResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class UserComponent {

    private final PasswordEncoder passwordEncoder;

    public UserComponent() {
        this.passwordEncoder = new BCryptPasswordEncoder();
    }

    public UserEntity buildUserEntity(MaintenanceUserRequest maintenanceUserRequest) {
        return UserEntity.builder()
                .email(maintenanceUserRequest.getEmail())
                .name(maintenanceUserRequest.getName())
                .password(passwordEncoder.encode(maintenanceUserRequest.getPassword()))
                .build();
    }

    public Page<UserResponse> buildUserResponsePage(Page<UserEntity> userEntities) {
        List<UserResponse> userResponses = userEntities.getContent().stream()
                .map(this::buildUserResponse)
                .toList();
        return new PageImpl<>(userResponses, userEntities.getPageable(), userEntities.getTotalElements());
    }

    public UserResponse buildUserResponse(UserEntity userEntity) {
        return UserResponse.builder()
                .email(userEntity.getEmail())
                .userId(userEntity.getId())
                .name(userEntity.getName())
                .build();
    }


}
