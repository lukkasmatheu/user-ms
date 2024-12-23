package com.vm.user.service;

import com.vm.user.component.UserComponent;
import com.vm.user.exceptions.GetUserException;
import com.vm.user.exceptions.UserMaintenanceException;
import com.vm.user.exceptions.UserNotFoundException;
import com.vm.user.model.entity.UserEntity;
import com.vm.user.model.request.MaintenanceUserRequest;
import com.vm.user.model.response.UserResponse;
import com.vm.user.repository.UserRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import static com.vm.user.utils.Utils.stringToPattern;

@Service
@AllArgsConstructor
@Slf4j
public class UserService {

    private UserRepository userRepository;
    private EmailService emailService;
    private UserComponent userComponent;

    public UserResponse createUser(MaintenanceUserRequest maintenanceUserRequest) {
        UserEntity user = userRepository.findByName(maintenanceUserRequest.getName());
        if (user != null) {
            return updateUser(user.getId(), maintenanceUserRequest);
        } else {
           return insertUser(maintenanceUserRequest);
        }
    }

    public UserResponse getUserById(String userId) throws GetUserException {
        log.info("Realizando busca de usuario com id:{}", userId);
        try {
            UserEntity userEntity = userRepository.findById(userId).orElseThrow(() -> new UserNotFoundException("Não foi possível encontrar o usuário."));
            return userComponent.buildUserResponse(userEntity);
        } catch (Exception exception) {
            log.error("Não foi possivel buscar o usuario com id {}, mensagem de erro: {}, Exception:", userId, exception.getMessage(), exception);
            throw new GetUserException("mensagem de erro:" + exception.getMessage() + " Exception: ", exception);
        }
    }

    public Page<UserResponse> getUser(String name, Pageable pageable){
        log.info("Realizando busca de lista de usuarios.");
        try {
            String namePattern = stringToPattern(name);
            Page<UserEntity> userEntity = userRepository.findByNameLike(namePattern, pageable);
            return userComponent.buildUserResponsePage(userEntity);
        } catch (Exception exception) {
            log.error("Não foi possivel buscar a lista de usuario, mensagem de erro: {}, Exception:", exception.getMessage(), exception);
            throw new GetUserException("mensagem de erro:" + exception.getMessage() + " Exception: ", exception);
        }
    }

    private UserResponse insertUser(MaintenanceUserRequest maintenanceUserRequest) {
        log.info("Realizando criação de novo usuario.");
        try {
            UserEntity userEntity = userComponent.buildUserEntity(maintenanceUserRequest);
            userEntity = userRepository.insert(userEntity);
            emailService.sendEmail("Usuario " + userEntity.getName() + " cadastrado com sucesso.", userEntity.getEmail());
            return userComponent.buildUserResponse(userEntity);
        } catch (Exception exception) {
            log.error("Não foi possivel criar novo usuario, mensagem de erro: {}, Exception:", exception.getMessage(), exception);
            throw new UserMaintenanceException("Não foi possivel salvar o usuario",exception);
        }
    }

    public UserResponse updateUser(String userId, MaintenanceUserRequest maintenanceUserRequest) {
        log.info("Realizando atualização de usuario.");
        try {
            UserEntity userEntity = userComponent.buildUserEntity(maintenanceUserRequest);
            userEntity.setId(userId);
            userEntity = userRepository.save(userEntity);
            emailService.sendEmail("Usuario " + userEntity.getName() + " Atualizado com sucesso.", userEntity.getEmail());
            return userComponent.buildUserResponse(userEntity);
        } catch (Exception exception) {
            log.error("Não foi possivel atualizar usuario, mensagem de erro: {}, Exception:", exception.getMessage(), exception);
            throw new UserMaintenanceException("Não foi possivel atualizar o usuario",exception);
        }
    }

    public boolean validateUser(String userId) {
        log.info("Valida se usuario existe.");
        try{
            UserResponse user = getUserById(userId);
            return user.getUserId() != null;
        }catch (Exception exception){
            log.error("Erro ao buscar usuario informado. mensagem de erro: {} , Exception: " , exception.getMessage(), exception);
            return false;
        }

    }
}
