package com.vm.user.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.vm.user.exceptions.UserNotFoundException;
import com.vm.user.model.request.MaintenanceUserRequest;
import com.vm.user.model.response.UserResponse;
import com.vm.user.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Collections;

import static com.vm.user.factory.UserFactory.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @InjectMocks
    private UserController userController;

    @Mock
    private UserService userService;

    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(userController)
                .setCustomArgumentResolvers(new PageableHandlerMethodArgumentResolver())
                .build();
        objectMapper = new ObjectMapper();
    }

    @Test
    void testCreateUserSuccess() throws Exception {
        var userResponse = buildValidResponse();
        var userRequest = buildValidRequest();

        when(userService.createUser(any(MaintenanceUserRequest.class))).thenReturn(userResponse);

        mockMvc.perform(post("/user")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userRequest)))
                .andExpect(status().isCreated())
                .andDo(print())
                .andExpect(jsonPath("$.email").value(userResponse.getEmail()))
                .andExpect(jsonPath("$.name").value(userResponse.getName()));
    }

    @Test
    void testCreateUserFailureBadRequest() throws Exception {
        var userResponse = buildValidResponse();
        var userRequest = buildInvalidRequest();

        mockMvc.perform(post("/user")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userRequest)))
                .andExpect(status().isBadRequest())
                .andDo(print());
    }

    @Test
    void testCreateUserFailure() throws Exception {

        MaintenanceUserRequest request = new MaintenanceUserRequest();

        mockMvc.perform(post("/user")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testUpdateUserSuccess() throws Exception {

        String userId = "1";
        var userResponse = buildValidResponse();
        var userRequest = buildValidRequest();

        when(userService.validateUser(anyString())).thenReturn(true);
        when(userService.updateUser(anyString(), any(MaintenanceUserRequest.class))).thenReturn(userResponse);

        mockMvc.perform(put("/user/{id_user}", userId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email").value(userResponse.getEmail()))
                .andExpect(jsonPath("$.name").value(userResponse.getName()));
    }

    @Test
    void testUpdateUserNotFound() throws Exception {

        String userId = "99";
        var userRequest = buildValidRequest();

        when(userService.validateUser(anyString())).thenReturn(false);

        mockMvc.perform(put("/user/{id_user}", userId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userRequest)))
                .andExpect(status().isNotFound());
    }

    @Test
    void testGetUsersSuccess() throws Exception {

        var userResponse = buildValidResponse();

        Page<UserResponse> expectedPage = new PageImpl<>(Collections.singletonList(userResponse), PageRequest.of(0, 10),1);
        when(userService.getUser(any(), any())).thenReturn(expectedPage);

        mockMvc.perform(get("/user")
                        .param("name", "Lucas")
                        .param("page", "0")
                        .param("size", "10")
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].email").value(userResponse.getEmail()))
                .andExpect(jsonPath("$.content[0].name").value(userResponse.getName()));
    }

    @Test
    void testGetUserByIdSuccess() throws Exception {

        String userId = "1";
        UserResponse expectedResponse = new UserResponse();
        expectedResponse.setEmail("lucas@gmail.com");
        expectedResponse.setName("Lucas");
        expectedResponse.setUserId("1L");

        when(userService.getUserById(userId)).thenReturn(expectedResponse);

        mockMvc.perform(get("/user/{id_user}", userId)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email").value(expectedResponse.getEmail()))
                .andExpect(jsonPath("$.name").value(expectedResponse.getName()));
    }

    @Test
    void testGetUserByIdNotFound() throws Exception {

        String userId = "99";
        when(userService.getUserById(userId)).thenThrow(UserNotFoundException.class);
        mockMvc.perform(get("/user/{id_user}", userId)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }
}
