package dev.elis.mapper;

import dev.elis.dto.user.UserResponseDTO;
import dev.elis.dto.user.UserSaveDTO;
import dev.elis.dto.user.UserUpdateDTO;
import dev.elis.model.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;


@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface UserDTOMapper {
    
    User toEntityInc(UserSaveDTO userSaveDTO);

    @Mapping(target = "name", source = "name")
    User toEntityUpd(UserUpdateDTO userUpdateDTO);

    UserResponseDTO toDTO(User user);

}