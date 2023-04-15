package br.com.mesdra.webfluxproject.mapper;

import br.com.mesdra.webfluxproject.entity.User;
import br.com.mesdra.webfluxproject.model.request.UserRequest;
import br.com.mesdra.webfluxproject.model.response.UserResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.NullValueCheckStrategy;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(
        componentModel = "spring",
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
        nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS
)
public interface UserMapper {
    @Mapping(target = "id",ignore = true)
    User toEntity(final UserRequest request);

    UserResponse toResponse(final User entity);
}
