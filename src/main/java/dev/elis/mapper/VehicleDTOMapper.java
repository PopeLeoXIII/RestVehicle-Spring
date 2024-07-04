package dev.elis.mapper;

import dev.elis.dto.vehicle.VehicleResponseDTO;
import dev.elis.dto.vehicle.VehicleSaveDTO;
import dev.elis.dto.vehicle.VehicleUpdateDTO;
import dev.elis.model.Vehicle;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;


@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface VehicleDTOMapper {

    Vehicle toEntityInc(VehicleSaveDTO vehicleSaveDTO);

    @Mapping(target = "name", source = "name")
    Vehicle toEntityUpd(VehicleUpdateDTO vehicleUpdateDTO);

    VehicleResponseDTO toDTO(Vehicle vehicle);

}