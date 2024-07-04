package dev.elis.mapper;

import dev.elis.dto.reservation.ReservationResponseDTO;
import dev.elis.dto.reservation.ReservationSaveDTO;
import dev.elis.dto.reservation.ReservationUpdateDTO;
import dev.elis.model.Reservation;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.Named;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.Date;


@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface ReservationDTOMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "startDatetime", source = "startDatetime", qualifiedByName = "timestampFromString")
    @Mapping(target = "endDatetime", source = "endDatetime", qualifiedByName = "timestampFromString")
    Reservation toEntityInc(ReservationSaveDTO reservationSaveDTO);

    @Mapping(target = "startDatetime", source = "startDatetime", qualifiedByName = "timestampFromString")
    @Mapping(target = "endDatetime", source = "endDatetime", qualifiedByName = "timestampFromString")
    Reservation toEntityUpd(ReservationUpdateDTO reservationUpdateDTO);

    ReservationResponseDTO toDTO(Reservation reservation);

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

}