package br.com.mesdra.webfluxproject.service;

import br.com.mesdra.webfluxproject.entity.User;
import br.com.mesdra.webfluxproject.mapper.UserMapper;
import br.com.mesdra.webfluxproject.model.request.UserRequest;
import br.com.mesdra.webfluxproject.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.Objects;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository repository;
    @Mock
    private UserMapper mapper;
    @InjectMocks
    private UserService service;

    @Test
    void testSave() {
        UserRequest request = new UserRequest("Vinicius", "VInicius@hotmail.com", "123");
        User entity = User.builder().build();

        when(mapper.toEntity(any())).thenReturn(entity);
        when(repository.save(any())).thenReturn(Mono.just(entity));

        Mono<User> result = service.save(request);

        StepVerifier.create(result).expectNextMatches(Objects::nonNull).expectComplete().verify();

        verify(repository, times(1)).save(any());
    }

    @Test
    void testFindByID() {
        when(repository.findById(any())).thenReturn(Mono.just(User.builder().id("1234").build()));

        Mono<User> result = service.findById("123");

        StepVerifier.create(result).expectNextMatches(user -> user.getClass() == User.class && Objects.equals(user.getId(), "1234")).expectComplete()
                    .verify();
        verify(repository, times(1)).findById(any());

    }

    @Test
    void testFindAll() {
        when(repository.findAll()).thenReturn(Flux.just(User.builder().id("1234").build()));

        Flux<User> result = service.findAll();

        StepVerifier.create(result).expectNextMatches(user -> user.getClass() == User.class && Objects.equals(user.getId(), "1234")).expectComplete()
                    .verify();

        verify(repository, times(1)).findAll();
    }

    @Test
    void testUpdate() {
        UserRequest request = new UserRequest("Vinicius", "VInicius@hotmail.com", "123");
        User entity = User.builder().build();

        when(mapper.toEntity(any(), any())).thenReturn(entity);
        when(repository.findById(any())).thenReturn(Mono.just(entity));
        when(repository.save(any())).thenReturn(Mono.just(entity));

        Mono<User> result = service.update("123", request);

        StepVerifier.create(result).expectNextMatches(Objects::nonNull).expectComplete().verify();

        verify(repository, times(1)).save(any());
    }

    @Test
    void testDelete() {
        UserRequest request = new UserRequest("Vinicius", "VInicius@hotmail.com", "123");
        User entity = User.builder().build();

        when(repository.findAndRemove(any())).thenReturn(Mono.just(entity));

        Mono<User> result = service.delete("123");

        StepVerifier.create(result).expectNextMatches(Objects::nonNull).expectComplete().verify();

        verify(repository, times(1)).findAndRemove(any());
    }
}