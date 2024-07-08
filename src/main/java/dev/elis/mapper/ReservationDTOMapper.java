package dev.elis.mapper;

import dev.elis.dto.city.CityUpdateDTO;
import dev.elis.dto.reservation.ReservationResponseDTO;
import dev.elis.dto.reservation.ReservationSaveDTO;
import dev.elis.dto.reservation.ReservationUpdateDTO;
import dev.elis.dto.user.UserUpdateDTO;
import dev.elis.dto.vehicle.VehicleResponseDTO;
import dev.elis.model.Reservation;
import dev.elis.model.Vehicle;
import org.mapstruct.*;
import org.mapstruct.factory.Mappers;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;


@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public abstract class ReservationDTOMapper {
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "startDatetime", source = "startDatetime", qualifiedByName = "timestampFromString")
    @Mapping(target = "endDatetime", source = "endDatetime", qualifiedByName = "timestampFromString")
    public abstract Reservation toEntityInc(ReservationSaveDTO reservationSaveDTO);

    @Mapping(target = "startDatetime", source = "startDatetime", qualifiedByName = "timestampFromString")
    @Mapping(target = "endDatetime", source = "endDatetime", qualifiedByName = "timestampFromString")
    public abstract Reservation toEntityUpd(ReservationUpdateDTO reservationUpdateDTO);

    @Named("timestampFromString")
    static Timestamp timestampFromString(String source) {
        try {
            DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm");
            Date parsedDate = dateFormat.parse(source);
            return new Timestamp(parsedDate.getTime());
        } catch (Exception e) {
            return Timestamp.valueOf(LocalDateTime.now());
        }
    }
    @Mapping(target = "vehicles", source = "vehicles", qualifiedByName = "toVehiclesDto")
    public abstract ReservationResponseDTO toDTO(Reservation reservation);

    @Named("toVehiclesDto")
    public Set<VehicleResponseDTO> map(Set<Vehicle> value) {
        if (value == null) {
            return Set.of();
        }
        return value.stream().map(this::vehicleToVehicleDto).collect(Collectors.toSet());
    }

    @Named("toVehicleDto")
    public VehicleResponseDTO vehicleToVehicleDto(Vehicle vehicle) {
        return new VehicleResponseDTO(
                vehicle.getId(),
                vehicle.getName(),
                new CityUpdateDTO(
                        vehicle.getCity().getId(),
                        vehicle.getCity().getName()
                ),
                Set.of()
        );
    }

}