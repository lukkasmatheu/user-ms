package com.vm.user.controller;


import com.vm.user.exceptions.UserNotFoundException;
import com.vm.user.model.request.MaintenanceUserRequest;
import com.vm.user.model.response.UserResponse;
import com.vm.user.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Create a new user", responses = {
            @ApiResponse(responseCode = "201", description = "User created successfully"),
            @ApiResponse(responseCode = "400", description = "Bad Request, invalid input informed", content = @Content(schema = @Schema(implementation = ResponseEntity.class))),
            @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content(schema = @Schema(implementation = ResponseEntity.class)))
    })
    public UserResponse createUser(@RequestBody @Valid MaintenanceUserRequest maintenanceUserRequest) {
        return userService.createUser(maintenanceUserRequest);
    }

    @PutMapping("/{id_user}")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Update an existing user", responses = {
            @ApiResponse(responseCode = "200", description = "User updated successfully"),
            @ApiResponse(responseCode = "400", description = "Bad Request, invalid input informed", content = @Content(schema = @Schema(implementation = ResponseEntity.class))),
            @ApiResponse(responseCode = "404", description = "User Not Found", content = @Content(schema = @Schema(implementation = ResponseEntity.class))),
            @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content(schema = @Schema(implementation = ResponseEntity.class)))
    })
    public UserResponse updateUser(@PathVariable(name = "id_user") String userId,
                                   @RequestBody @Valid MaintenanceUserRequest maintenanceUserRequest) {
        if(userService.validateUser(userId)){
            return userService.updateUser(userId, maintenanceUserRequest);
        }else{
            throw new UserNotFoundException("Erro ao buscar usuario informado, verique o id e tente novamente");
        }
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Get a list of users", responses = {
            @ApiResponse(responseCode = "200", description = "Successful retrieval of users"),
            @ApiResponse(responseCode = "400", description = "Bad Request, invalid input informed", content = @Content(schema = @Schema(implementation = ResponseEntity.class))),
            @ApiResponse(responseCode = "404", description = "User Not Found", content = @Content(schema = @Schema(implementation = ResponseEntity.class))),
            @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content(schema = @Schema(implementation = ResponseEntity.class)))
    })
    public Page<UserResponse> getUsers(@RequestParam(required = false) String name, Pageable pageable) {
        return userService.getUser(name, pageable);
    }

    @GetMapping("/{id_user}")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Get a user by ID", responses = {
            @ApiResponse(responseCode = "200", description = "User found"),
            @ApiResponse(responseCode = "400", description = "Bad Request, invalid input informed", content = @Content(schema = @Schema(implementation = ResponseEntity.class))),
            @ApiResponse(responseCode = "404", description = "User Not Found", content = @Content(schema = @Schema(implementation = ResponseEntity.class))),
            @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content(schema = @Schema(implementation = ResponseEntity.class)))
    })
    public UserResponse getUser(@PathVariable(name = "id_user") String userId) {
        return userService.getUserById(userId);
    }
}
