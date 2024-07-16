package dev.elis.service;

import dev.elis.dto.reservation.ReservationResponseDTO;
import dev.elis.dto.reservation.ReservationSaveDTO;
import dev.elis.dto.reservation.ReservationUpdateDTO;
import dev.elis.exception.BadRequestException;
import dev.elis.exception.NotFoundException;
import dev.elis.mapper.ReservationDTOMapper;
import dev.elis.model.Reservation;
import dev.elis.model.Vehicle;
import dev.elis.repository.ReservationRepository;
import dev.elis.repository.VehicleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class ReservationService {

    public static final String EMPTY_RESERVATION_USER = "Reservation user cannot be empty";
    public static final String EMPTY_RESERVATION_STATUS = "Reservation status cannot be empty";
    public static final String RESERVATION_WRONG_DATE = "Reservation wrong date";
    public static final String RESERVATION_DOES_NOT_EXIST = "This Reservation does not exist!";
    private final ReservationRepository reservationRepository;
    private final VehicleRepository vehicleRepository;
    private final ReservationDTOMapper dtoMapper;

    @Autowired
    public ReservationService(ReservationRepository reservationRepository, VehicleRepository vehicleRepository, ReservationDTOMapper dtoMapper) {
        this.reservationRepository = reservationRepository;
        this.vehicleRepository = vehicleRepository;
        this.dtoMapper = dtoMapper;
    }

    @Transactional
    public ReservationResponseDTO save(ReservationSaveDTO dto) {
        Reservation reservation = dtoMapper.toEntityInc(dto);
        validateStatusCityAndDate(reservation);

        Set<Vehicle> vehicleSet  = new HashSet<>();
        for(Vehicle vehicle : reservation.getVehicles()) {
            vehicleRepository.findById(vehicle.getId()).ifPresent(vehicleSet::add);
        }
        vehicleSet.forEach(v -> v.addReservation(reservation));
        reservation.setVehicles(vehicleSet);

        Reservation savedReservation = reservationRepository.save(reservation);

        return dtoMapper.toDTO(savedReservation);
    }

    @Transactional
    public void update(ReservationUpdateDTO dto) throws NotFoundException {
        checkExist(dto.getId());
        Reservation newReservation = dtoMapper.toEntityUpd(dto);
        validateStatusCityAndDate(newReservation);

        Set<Vehicle> newVehicles  = new HashSet<>();
        for(Vehicle vehicle : newReservation.getVehicles()) {
            vehicleRepository.findById(vehicle.getId()).ifPresent(newVehicles::add);
        }

        reservationRepository.findById(newReservation.getId()).ifPresent(
                r -> updateReservationVehicles(r, newVehicles)
        );

        reservationRepository.save(newReservation);
    }

    private void updateReservationVehicles(Reservation r, Set<Vehicle> newVehicles) {
        Set<Vehicle> currentVehicles = new HashSet<>(r.getVehicles());

        currentVehicles.forEach(v -> {
            if(!newVehicles.contains(v)) {
                v.removeReservation(r);
            }
        });

        newVehicles.forEach(v -> {
            if(!currentVehicles.contains(v)) {
                v.addReservation(r);
            }
        });
    }

    @Transactional
    public ReservationResponseDTO findById(Long id) throws NotFoundException {
        Reservation reservation = reservationRepository.findById(id).orElseThrow(() ->
                new NotFoundException(RESERVATION_DOES_NOT_EXIST));
        return dtoMapper.toDTO(reservation);
    }

    @Transactional
    public List<ReservationResponseDTO> findAll() {
        List<Reservation> cities = reservationRepository.findAll();
        return cities.stream()
                .map(dtoMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Transactional
    public boolean delete(Long id) throws NotFoundException {
        checkExist(id);
        reservationRepository.deleteById(id);
        return true;
    }

    private void checkExist(Long id) throws NotFoundException {
        if (!reservationRepository.existsById(id)) {
            throw new NotFoundException(RESERVATION_DOES_NOT_EXIST);
        }
    }

    private static void validateStatusCityAndDate(Reservation reservation) {
        if (reservation.getStatus() == null) {
            throw new BadRequestException(EMPTY_RESERVATION_STATUS);
        }

        if (reservation.getUser() == null) {
            throw new BadRequestException(EMPTY_RESERVATION_USER);
        }

        if (reservation.getStartDatetime() == null || reservation.getEndDatetime() == null
                || reservation.getEndDatetime().before(reservation.getStartDatetime())) {
            throw new BadRequestException(RESERVATION_WRONG_DATE);
        }
    }
}