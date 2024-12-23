package com.vm.user.service;

import com.vm.user.component.UserComponent;
import com.vm.user.exceptions.GetUserException;
import com.vm.user.exceptions.UserMaintenanceException;
import com.vm.user.model.entity.UserEntity;
import com.vm.user.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.Collections;
import java.util.Optional;

import static com.vm.user.factory.UserFactory.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @InjectMocks
    private UserService userService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private EmailService emailService;

    @Mock
    private UserComponent userComponent;

    @Test
    void testCreateUser(){
        var userRequest = buildValidRequest();
        var userEntity = buildValidEntity();
        var userResponse = buildValidResponse();

        when(userRepository.findByName(any())).thenReturn(null);
        when(userRepository.insert(userEntity)).thenReturn(userEntity);
        when(userComponent.buildUserEntity(any())).thenReturn(userEntity);
        when(userComponent.buildUserResponse(any())).thenReturn(userResponse);

        var userResult = userService.createUser(userRequest);

        assertNotNull(userResult);
        assertEquals(userResult.getEmail(),userEntity.getEmail());
        assertEquals(userResult.getName(),userEntity.getName());
        assertEquals(userResult.getUserId(),userEntity.getId());

        verify(userRepository,times(1)).insert(userEntity);
    }

    @Test
    void testCreateUserFailure(){
        var userRequest = buildValidRequest();
        var userEntity = buildValidEntity();

        when(userRepository.findByName(any())).thenReturn(null);
        when(userRepository.insert(userEntity)).thenThrow(new RuntimeException("Não foi possivel estabelecer conexão com o banco"));
        when(userComponent.buildUserEntity(any())).thenReturn(userEntity);

        assertThrows(UserMaintenanceException.class, () -> userService.createUser(userRequest));

        verify(userRepository,times(1)).insert(userEntity);
        verifyNoInteractions(emailService);
    }

    @Test
    void testUpdateUserBySameName(){
        var userRequest = buildValidRequest();
        userRequest.setEmail("lucas.matheus@gmail.com");
        var userEntity = buildValidEntity();
        var userResponse = buildValidResponse();

        when(userRepository.findByName(any())).thenReturn(userEntity);
        when(userComponent.buildUserEntity(any())).thenReturn(userEntity);
        when(userRepository.save(any(UserEntity.class))).thenReturn(userEntity);
        when(userComponent.buildUserResponse(any())).thenReturn(userResponse);

        var userResult = userService.createUser(userRequest);

        assertNotNull(userResult);
        assertEquals(userResult.getEmail(),userEntity.getEmail());
        assertEquals(userResult.getName(),userEntity.getName());
        assertEquals(userResult.getUserId(),userEntity.getId());

        verify(userRepository,times(1)).save(userEntity);
    }

    @Test
    void testUpdateUserBySameNameFailure(){
        var userRequest = buildValidRequest();
        userRequest.setEmail("lucas.matheus@gmail.com");
        var userEntity = buildValidEntity();

        when(userRepository.findByName(any())).thenReturn(userEntity);
        when(userRepository.save(userEntity)).thenThrow(new RuntimeException("Não foi possivel estabelecer conexão com o banco"));
        when(userComponent.buildUserEntity(any())).thenReturn(userEntity);

        assertThrows(UserMaintenanceException.class, () -> userService.createUser(userRequest));

        verify(userRepository,times(1)).save(userEntity);
        verifyNoInteractions(emailService);
    }

    @Test
    void testGetUserById(){
        String userId = "123";
        var userEntity = buildValidEntity();
        var userResponse = buildValidResponse();

        when(userComponent.buildUserResponse(any())).thenReturn(userResponse);
        when(userRepository.findById(any())).thenReturn(Optional.of(userEntity));

        var result = userService.validateUser(userId);

        assertTrue(result);
        verify(userRepository,times(1)).findById(any());
    }

    @Test
    void testGetUserByIdFailure(){
        String userId = "123";

        when(userRepository.findById(any())).thenReturn(null);

        var result = userService.validateUser(userId);

        assertFalse(result);
        verify(userRepository,times(1)).findById(any());
    }

    @Test
    void testGetUserByNameLike(){
        String nameUser = "Lucas";
        var userEntity = buildValidEntity();
        var userResponse = buildValidResponse();

        var expectedPage = new PageImpl<>(Collections.singletonList(userEntity), PageRequest.of(0, 10),1);
        var responsePage = new PageImpl<>(Collections.singletonList(userResponse), PageRequest.of(0, 10),1);


        when(userComponent.buildUserResponsePage(any())).thenReturn(responsePage);
        when(userRepository.findByNameLike(any(),any())).thenReturn(expectedPage);

        var result = userService.getUser(nameUser,Pageable.ofSize(10));

        assertEquals(1, result.getTotalElements());
        assertEquals(userEntity.getName(), result.getContent().getFirst().getName());
        assertTrue(result.getContent().getFirst().getName().toLowerCase().contains(nameUser.toLowerCase()));
        verify(userRepository,times(1)).findByNameLike(any(),any());
    }

    @Test
    void testGetUserByNameLikeFailure(){
        String nameUser = "Lucas";

        when(userRepository.findByNameLike(any(),any())).thenThrow(new RuntimeException("Erro ao realizar busca de usuarios."));

        assertThrows(GetUserException.class, () -> userService.getUser(nameUser,Pageable.ofSize(10)));

    }


}
