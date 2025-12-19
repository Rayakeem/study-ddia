package com.ddia.ddia_labs.ch01;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
public class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @Test
    @DisplayName("유저를 저장하고 불러오면 같은 데이터야 한다!")
    void saveAndFind () {
        //given
        User user = new User("sohee", "CTO");

        //when
        User savedUser = userRepository.save(user);

        //then
        assertThat(savedUser.getId()).isNotNull();
        assertThat(savedUser.getName()).isEqualTo("sohee");
        assertThat(savedUser.getRole()).isEqualTo("CTO");
    }
}
