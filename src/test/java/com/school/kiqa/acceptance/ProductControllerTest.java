package com.school.kiqa.acceptance;

import com.school.kiqa.command.dto.product.ProductDetailsDto;
import com.school.kiqa.command.dto.user.UserDetailsDto;
import com.school.kiqa.persistence.entity.UserEntity;
import com.school.kiqa.persistence.repository.ProductRepository;
import com.school.kiqa.security.UserAuthenticationProvider;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.junit.Before;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Objects;
import java.util.Optional;

import static com.school.kiqa.MockedData.getCreateUserDto;
import static com.school.kiqa.MockedData.getMockedProductEntity;
import static com.school.kiqa.MockedData.getMockedUserEntity;
import static com.school.kiqa.MockedData.getProductDetailsDto;
import static com.school.kiqa.MockedData.getUserDetailsDto;
import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ProductControllerTest {


    @MockBean
    private ProductRepository productRepository;

    @MockBean
    private UserAuthenticationProvider userAuthenticationProvider;

    @MockBean
    private PasswordEncoder passwordEncoder;

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Before
    public void setUp() {
        RestAssured.port = port;
    }


    @Nested
    class GetProductById {
        @Test
        void test_getProductById_shouldReturn200() {
            // arrange
            final var entity = getMockedProductEntity();

            when(productRepository.findById(6L))
                    .thenReturn(Optional.of(entity));


            String path = "/products/6";

            // WHEN
            ResponseEntity<ProductDetailsDto> response = restTemplate.exchange(
                    path,
                    HttpMethod.GET,
                    HttpEntity.EMPTY,
                    ProductDetailsDto.class);

            // THEN
            verify(productRepository, times(1))
                    .findById(anyLong());

            final var expected = getProductDetailsDto(entity).getId();
            assertEquals(expected, Objects.requireNonNull(response.getBody()).getId());

/*
            // act
            final var response = given()
                    .port(port)
                    .contentType(ContentType.JSON)
                    .when()
                    .get("/products/6")
                    .then().extract().response();

            // assert
            verify(productRepository, times(1))
                    .findById(anyLong());

            assertEquals(HttpStatus.OK.value(), response.statusCode());

            final var expected = getProductDetailsDto(entity);

            final var actual = response.getBody().as(ProductDetailsDto.class);
            assertEquals(expected, actual); */
        }
    }



}
