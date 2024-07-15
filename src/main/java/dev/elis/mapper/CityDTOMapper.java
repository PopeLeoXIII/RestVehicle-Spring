package dev.elis.mapper;

import dev.elis.dto.city.*;
import dev.elis.model.City;
import dev.elis.model.Vehicle;
import org.mapstruct.*;

import java.util.List;


@Mapper(componentModel = MappingConstants.ComponentModel.SPRING, unmappedTargetPolicy = ReportingPolicy.WARN)
public abstract class CityDTOMapper {
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "vehicles", expression = "java(emptyList())")
    public abstract City toEntityInc(CitySaveDTO citySaveDTO);

    @Mapping(target = "vehicles", expression = "java(emptyList())")
    public abstract City toEntityUpd(CityUpdateDTO cityUpdateDTO);

    protected List<Vehicle> emptyList() {
        return  List.of();
    }

    public abstract CityResponseDTO toDTO(City city);

    @AfterMapping
    protected void ignoreFathersChildren(City city, @MappingTarget CityResponseDTO cityResponseDTO) {
        if (cityResponseDTO.getVehicles() != null){
            cityResponseDTO.getVehicles().forEach(v -> v.setCity(null));
        }
    }
}