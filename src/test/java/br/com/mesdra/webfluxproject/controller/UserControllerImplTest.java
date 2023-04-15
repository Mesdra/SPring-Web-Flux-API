package br.com.mesdra.webfluxproject.controller;

import br.com.mesdra.webfluxproject.entity.User;
import br.com.mesdra.webfluxproject.mapper.UserMapper;
import br.com.mesdra.webfluxproject.model.request.UserRequest;
import br.com.mesdra.webfluxproject.model.response.UserResponse;
import br.com.mesdra.webfluxproject.service.UserService;
import br.com.mesdra.webfluxproject.service.exception.ObjectNotFoundException;
import com.mongodb.reactivestreams.client.MongoClient;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.reactive.function.BodyInserters;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureWebTestClient
class UserControllerImplTest {

    public static final String EMAIL = "vinicius@hotmail.com";
    public static final String PASSWORD = "123";
    public static final String NAME = "vinicius";
    @Autowired
    private WebTestClient webTestClient;

    @MockBean
    private UserService service;

    @MockBean
    private UserMapper mapper;

    @MockBean
    private MongoClient mongoClient;

    @Test
    @DisplayName("Teste de sucesso ao salvar")
    void testSaveWithSuccess() {
        UserRequest request = new UserRequest(NAME, EMAIL, PASSWORD);

        when(service.save(any())).thenReturn(Mono.just(User.builder().build()));

        webTestClient.post().uri("/users").contentType(MediaType.APPLICATION_JSON).body(BodyInserters.fromValue(request)).exchange().expectStatus()
                     .isCreated();

        verify(service, times(1)).save(any());
    }

    @Test
    @DisplayName("Teste de falha ao salvar")
    void testSaveWithFail() {
        UserRequest request = new UserRequest(" vinicius", EMAIL, PASSWORD);

        webTestClient.post().uri("/users")
                     .contentType(MediaType.APPLICATION_JSON)
                     .body(BodyInserters.fromValue(request))
                     .exchange()
                     .expectStatus()
                     .isBadRequest()
                     .expectBody()
                     .jsonPath("$.path").isEqualTo("/users")
                     .jsonPath("$.status").isEqualTo(HttpStatus.BAD_REQUEST.value())
                     .jsonPath("$.error").isEqualTo("Validation Error")
                     .jsonPath("$.message").isEqualTo("Erro de validação de formulario")
                     .jsonPath("$.errors[0].fieldName").isEqualTo("name")
                     .jsonPath("$.errors[0].message").isEqualTo("Campo não pode contem espaços em branco no início ou fim");

        UserRequest request2 = new UserRequest(NAME, "viniciushotmail.com", PASSWORD);

        webTestClient.post().uri("/users")
                     .contentType(MediaType.APPLICATION_JSON)
                     .body(BodyInserters.fromValue(request2))
                     .exchange()
                     .expectStatus()
                     .isBadRequest()
                     .expectBody()
                     .jsonPath("$.path").isEqualTo("/users")
                     .jsonPath("$.status").isEqualTo(HttpStatus.BAD_REQUEST.value())
                     .jsonPath("$.error").isEqualTo("Validation Error")
                     .jsonPath("$.message").isEqualTo("Erro de validação de formulario")
                     .jsonPath("$.errors[0].fieldName").isEqualTo("email")
                     .jsonPath("$.errors[0].message").isEqualTo("Email Invalido");

    }

    @Test
    @DisplayName("Teste de sucesso ao buscar por Id ")
    void testFindWithSuccess() {

        final UserResponse response = new UserResponse("123456", NAME, EMAIL, PASSWORD);

        when(service.findById(any())).thenReturn(Mono.just(User.builder().build()));
        when(mapper.toResponse(any(User.class))).thenReturn(response);

        webTestClient.get().uri("/users/" + "123456")
                     .accept(MediaType.APPLICATION_JSON)
                     .exchange().expectStatus().isOk().expectBody()
                     .jsonPath("$.id").isEqualTo("123456")
                     .jsonPath("$.name").isEqualTo(NAME)
                     .jsonPath("$.email").isEqualTo(EMAIL)
                     .jsonPath("$.password").isEqualTo(PASSWORD);


    }

    @Test
    @DisplayName("Teste de falha ao buscar por Id ")
    void testfindWithFail() {

        final UserResponse response = new UserResponse("123456", NAME, EMAIL, PASSWORD);

        when(service.findById(any())).thenThrow(new ObjectNotFoundException(String.format("Objeto não encontrado. Id %s Tipo %s", "123456",
                                                                                          User.class.getSimpleName())));
        when(mapper.toResponse(any(User.class))).thenReturn(response);

        webTestClient.get().uri("/users/" + "123456")
                     .accept(MediaType.APPLICATION_JSON)
                     .exchange().expectStatus().isNotFound()
                     .expectBody()
                     .jsonPath("$.path").isEqualTo("/users/123456");


    }

    @Test
    @DisplayName("Teste de sucesso ao buscar todos ")
    void testFindALlWithSuccess() {

        final UserResponse response = new UserResponse("123456", NAME, EMAIL, PASSWORD);

        when(service.findAll()).thenReturn(Flux.just(User.builder().build()));
        when(mapper.toResponse(any(User.class))).thenReturn(response);

        webTestClient.get().uri("/users")
                     .accept(MediaType.APPLICATION_JSON)
                     .exchange().expectStatus().isOk().expectBody()
                     .jsonPath("$.[0].id").isEqualTo("123456")
                     .jsonPath("$.[0].name").isEqualTo(NAME)
                     .jsonPath("$.[0].email").isEqualTo(EMAIL)
                     .jsonPath("$.[0].password").isEqualTo(PASSWORD);


    }

    @Test
    @DisplayName("Teste de sucesso Update")
    void testUpdate() {
        final UserResponse response = new UserResponse("123456", NAME, EMAIL, PASSWORD);
        UserRequest request = new UserRequest(NAME, EMAIL, PASSWORD);
        when(mapper.toResponse(any(User.class))).thenReturn(response);
        when(service.update(any(), any())).thenReturn(Mono.just(User.builder().build()));

        webTestClient.patch().uri("/users/" + "123456")
                     .contentType(MediaType.APPLICATION_JSON)
                     .body(BodyInserters.fromValue(request))
                     .exchange()
                     .expectStatus().isOk()
                     .expectBody().
                     jsonPath("$.id").isEqualTo("123456")
                     .jsonPath("$.name").isEqualTo(NAME)
                     .jsonPath("$.email").isEqualTo(EMAIL)
                     .jsonPath("$.password").isEqualTo(PASSWORD);

        verify(service).update(anyString(), any());
        verify(mapper).toResponse(any());
    }

    @Test
    void testDelete() {

        when(service.delete(any())).thenReturn(Mono.just(User.builder().build()));

        webTestClient.delete().uri("/users/" + "123456")
                     .exchange().expectStatus().isOk();

        verify(service).delete(anyString());
    }
}