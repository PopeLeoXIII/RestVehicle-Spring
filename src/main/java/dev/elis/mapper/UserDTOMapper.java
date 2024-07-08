package dev.elis.mapper;

import dev.elis.dto.user.UserResponseDTO;
import dev.elis.dto.user.UserSaveDTO;
import dev.elis.dto.user.UserUpdateDTO;
import dev.elis.model.Reservation;
import dev.elis.model.User;
import org.mapstruct.*;

import java.util.List;


@Mapper(componentModel = MappingConstants.ComponentModel.SPRING, unmappedTargetPolicy = ReportingPolicy.WARN)
public abstract class UserDTOMapper {

    protected List<Reservation> emptyList() {
        return  List.of();
    }

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "reservations", expression = "java(emptyList())")
    public abstract User toEntityInc(UserSaveDTO userSaveDTO);

    @Mapping(target = "reservations", expression = "java(emptyList())")
    public abstract User toEntityUpd(UserUpdateDTO userUpdateDTO);

    public abstract UserResponseDTO toDTO(User user);

    @AfterMapping
    protected void ignoreFathersChildren(User user, @MappingTarget UserResponseDTO userResponseDTO) {
        if (userResponseDTO.getReservations() != null){
            userResponseDTO.getReservations().forEach(v -> v.setUser(null));
        }
    }
}