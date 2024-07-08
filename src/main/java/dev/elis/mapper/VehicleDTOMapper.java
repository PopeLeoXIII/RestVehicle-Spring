package dev.elis.mapper;

import dev.elis.dto.reservation.ReservationResponseDTO;
import dev.elis.dto.user.UserUpdateDTO;
import dev.elis.dto.vehicle.VehicleResponseDTO;
import dev.elis.dto.vehicle.VehicleSaveDTO;
import dev.elis.dto.vehicle.VehicleUpdateDTO;
import dev.elis.model.Reservation;
import dev.elis.model.Vehicle;
import org.mapstruct.*;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public abstract class VehicleDTOMapper {

    @Mapping(target = "id", ignore = true)
    public abstract Vehicle toEntityInc(VehicleSaveDTO vehicleSaveDTO);

    public abstract Vehicle toEntityUpd(VehicleUpdateDTO vehicleUpdateDTO);

    @Mapping(target = "reservations", qualifiedByName = "toReservationsDto")
    public abstract VehicleResponseDTO toDTO(Vehicle vehicle);

    @Named("toReservationsDto")
    public Set<ReservationResponseDTO> map(Set<Reservation> value) {
        if (value == null) {
            return Set.of();
        }
        return value.stream().map(this::reservationToReservationDto).collect(Collectors.toSet());
    }

    @Named("toReservationDto")
    public ReservationResponseDTO reservationToReservationDto(Reservation reservation) {
        return new ReservationResponseDTO(
                reservation.getId(),
                reservation.getStatus().toString(),
                reservation.getStartDatetime().toString(),
                reservation.getEndDatetime().toString(),
                new UserUpdateDTO(
                        reservation.getUser().getId(),
                        reservation.getUser().getName(),
                        reservation.getUser().getSurname()
                ),
                Set.of()
        );
    }
}
