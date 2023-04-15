package br.com.mesdra.webfluxproject.mapper;

import br.com.mesdra.webfluxproject.entity.User;
import br.com.mesdra.webfluxproject.model.request.UserRequest;
import br.com.mesdra.webfluxproject.model.response.UserResponse;
import org.mapstruct.*;

@Mapper(
        componentModel = "spring",
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
        nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS
)
public interface UserMapper {
    @Mapping(target = "id",ignore = true)
    User toEntity(final UserRequest request);

    @Mapping(target = "id",ignore = true)
    User toEntity(final UserRequest request,@MappingTarget final User user);

    UserResponse toResponse(final User entity);
}
