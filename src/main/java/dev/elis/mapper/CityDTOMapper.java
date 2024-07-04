package dev.elis.mapper;

import dev.elis.dto.city.*;
import dev.elis.model.City;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;


@Mapper(componentModel = MappingConstants.ComponentModel.SPRING, unmappedTargetPolicy = ReportingPolicy.WARN)
public interface CityDTOMapper {

    City toEntityInc(CitySaveDTO citySaveDTO);

//    @Mapping(target = "name", source = "name")
    City toEntityUpd(CityUpdateDTO cityUpdateDTO);

    CityResponseDTO toDTO(City city);

}