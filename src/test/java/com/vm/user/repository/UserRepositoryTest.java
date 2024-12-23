package com.vm.user.repository;


import com.vm.user.model.entity.UserEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.mongodb.core.MongoTemplate;

import static com.vm.user.factory.UserFactory.buildValidEntity;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;


@SpringBootTest
class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private MongoTemplate mongoTemplate;


    @BeforeEach
    void setUp() {
        mongoTemplate.dropCollection(UserEntity.class);
        var userEntity = buildValidEntity();
        userRepository.save(userEntity);
    }

    @Test
    void testFindByName() {

        String userName = "Lucas Matheus";

        UserEntity foundUser = userRepository.findByName(userName);

        assertNotNull(foundUser);
        assertEquals(userName, foundUser.getName());
    }

    @Test
    void testFindByNameLike() {
        Page<UserEntity> userPage = userRepository.findByNameLike("Lu", PageRequest.of(0, 10));

        assertNotNull(userPage);
        assertEquals(1,userPage.getTotalElements());
        assertEquals("Lucas Matheus", userPage.getContent().getFirst().getName());
    }
}
