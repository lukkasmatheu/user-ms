package com.vm.user.component;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.util.Collections;

import static com.vm.user.factory.UserFactory.buildValidEntity;
import static com.vm.user.factory.UserFactory.buildValidRequest;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

@ExtendWith(MockitoExtension.class)
public class UserComponentTest {
    @InjectMocks
    private UserComponent userComponent;

    @Test
    void parseMaintenanceToUserEntity(){
        var userRequest = buildValidRequest();
        var userEntity = userComponent.buildUserEntity(userRequest);
        assertEquals(userEntity.getName(),userRequest.getName());
        assertEquals(userEntity.getEmail(),userRequest.getEmail());
    }

    @Test
    void parseUserEntityToUserResponse(){
        var userEntity = buildValidEntity();
        var userResponse = userComponent.buildUserResponse(userEntity);
        assertEquals(userEntity.getName(),userResponse.getName());
        assertEquals(userEntity.getEmail(),userResponse.getEmail());
    }

    @Test
    void parseUserEntityToUserResponsePage(){
        var userEntity = buildValidEntity();
        var expectedPage = new PageImpl<>(Collections.singletonList(userEntity), PageRequest.of(0, 10),1);
        var resultPage = userComponent.buildUserResponsePage(expectedPage);
        assertEquals(resultPage.getContent().getFirst().getName(),expectedPage.getContent().getFirst().getName());
        assertEquals(resultPage.getContent().getFirst().getEmail(),expectedPage.getContent().getFirst().getEmail());
    }

}
