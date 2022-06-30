package com.school.kiqa.acceptance;

import com.school.kiqa.command.dto.user.UserDetailsDto;
import com.school.kiqa.persistence.entity.UserEntity;
import com.school.kiqa.persistence.repository.UserRepository;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.junit.Before;

import java.util.Optional;

import static io.restassured.RestAssured.given;
import static com.school.kiqa.MockedData.getMockedUserEntity;
import static com.school.kiqa.MockedData.getUserDetailsDto;
import static com.school.kiqa.MockedData.getCreateUserDto;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class UserControllerTest {

    @MockBean
    private UserRepository userRepository;

    @MockBean
    private PasswordEncoder passwordEncoder;

    @LocalServerPort
    private int port;

    @Before
    public void setUp() {
        RestAssured.port = port;
    }

    @Nested
    class CreateUser {
        @Test
        void test_createUser_shouldReturn200() {
            // arrange
            final var entity = getMockedUserEntity();

            when(userRepository.save(Mockito.any(UserEntity.class))).thenReturn(entity);
            when(userRepository.findByEmail(Mockito.any(String.class)))
                    .thenReturn(Optional.empty());

            // act
            final var response = given()
                    .port(port)
                    .body(getCreateUserDto())
                    .contentType(ContentType.JSON)
                    .when()
                    .post("/users")
                    .then().extract().response();

            // assert
            verify(userRepository, times(1)).findByEmail(anyString());
            verify(userRepository, times(1)).save(any(UserEntity.class));

            assertEquals(HttpStatus.OK.value(), response.statusCode());

            final var expected = getUserDetailsDto(entity);
            final var actual = response.getBody().as(UserDetailsDto.class);

            assertEquals(expected, actual);
        }
    }

}
