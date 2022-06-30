package com.school.kiqa.acceptance;

import com.school.kiqa.MockedData;
import com.school.kiqa.command.dto.auth.CredentialsDto;
import com.school.kiqa.command.dto.auth.PrincipalDto;
import com.school.kiqa.command.dto.user.UserDetailsDto;
import com.school.kiqa.controller.AuthController;
import com.school.kiqa.persistence.entity.UserEntity;
import com.school.kiqa.persistence.repository.UserRepository;
import com.school.kiqa.security.UserAuthenticationProvider;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.http.Cookie;
import org.checkerframework.checker.nullness.Opt;
import org.junit.Before;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Collections;
import java.util.Optional;

import static com.school.kiqa.MockedData.*;
import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class UserControllerTest {

    @MockBean
    private UserRepository userRepository;

    @MockBean
    private UserAuthenticationProvider userAuthenticationProvider;

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
            verify(userRepository, times(1)).save(any(UserEntity.class));
            verify(userRepository, times(1)).findByEmail(anyString());

            assertEquals(HttpStatus.OK.value(), response.statusCode());

            final var expected = getUserDetailsDto(entity).getId();

            final var actual = response.getBody().as(UserDetailsDto.class).getId();
            assertEquals(expected, actual);
        }
    }


    @Nested
    class GetAllUsers {
        @Test
        void test_getAllUsers_shouldReturn200() {

            // arrange
            when(userRepository.findAll())
                    .thenReturn(getUserList());

            when(userAuthenticationProvider.validateToken(Mockito.anyString()))
                    .thenReturn(new UsernamePasswordAuthenticationToken(
                            PrincipalDto.builder()
                                    .id(getMockedAdmin().getId())
                                    .name(getMockedAdmin().getName())
                                    .role(getMockedAdmin().getUserType())
                                    .build(),
                            null,
                            Collections.singletonList(new SimpleGrantedAuthority("ADMIN"))));

            // act
            final var response = given()
                    .port(port)
                    .cookie(new Cookie.Builder("cookie_auth", "some_value")
                            .setSecured(true)
                            .setHttpOnly(true)
                            .setSameSite("None")
                            .setPath("/")
                            .build())
                    .contentType(ContentType.JSON)
                    .when()
                    .get("/users")
                    .then().extract().response();

            // assert
            //verify(userRepository, times(1)).findAll();

            final var expected = getUserList().stream().map(MockedData::getUserDetailsDto).toArray();

            assertEquals(expected[0], response.getBody().as(UserDetailsDto[].class)[0]);
        }

    }


   /* @Test
    void test_getUserById_shouldReturn200() {
        // GIVEN
        UserEntity entity = getMockedUserEntity();
        when(userRepository.findById(5L))
                .thenReturn(Optional.of(entity));
        String path = "/users/5";

        // WHEN
        ResponseEntity<UserDetailsDto> response = restTemplate.exchange(
                path,
                HttpMethod.GET,
                HttpEntity.EMPTY,
                UserDetailsDto.class);

        // THEN
        verify(userRepository, times(1))
                .findById(anyLong());

        UserDetailsDto expected = getUserDetailsDto(entity);
        assertEquals(expected, response.getBody());
    } */

}
