package br.com.mesdra.webfluxproject.service;

import br.com.mesdra.webfluxproject.entity.User;
import br.com.mesdra.webfluxproject.mapper.UserMapper;
import br.com.mesdra.webfluxproject.model.request.UserRequest;
import br.com.mesdra.webfluxproject.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class UserService {


    private final UserRepository repository;
    private final UserMapper mapper;

    public Mono<User> save(final UserRequest request) {
        return repository.save(mapper.toEntity(request));
    }
}
