package dev.elis.service;

import dev.elis.TestData;
import dev.elis.dto.reservation.ReservationResponseDTO;
import dev.elis.dto.reservation.ReservationSaveDTO;
import dev.elis.dto.reservation.ReservationUpdateDTO;
import dev.elis.dto.user.UserUpdateDTO;
import dev.elis.dto.vehicle.VehicleUpdateDTO;
import dev.elis.exception.BadRequestException;
import dev.elis.exception.NotFoundException;
import dev.elis.mapper.ReservationDTOMapperImpl;
import dev.elis.model.Reservation;
import dev.elis.model.Vehicle;
import dev.elis.repository.ReservationRepository;
import dev.elis.repository.VehicleRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.text.SimpleDateFormat;
import java.util.*;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ReservationServiceTest {
    private final ReservationRepository reservationRepository = mock(ReservationRepository.class);
    private final VehicleRepository vehicleRepository = mock(VehicleRepository.class);
    private final ReservationDTOMapperImpl reservationDTOMapper = new ReservationDTOMapperImpl();
    private ReservationService reservationService;

    private Reservation reservation;
    private ReservationSaveDTO saveDTO;
    private ReservationUpdateDTO updateDTO;
    private Vehicle vehicle;

    @BeforeEach
    public void setUp() {
        reservationService = new ReservationService(reservationRepository, vehicleRepository, reservationDTOMapper);

        TestData testData = new TestData();
        reservation = testData.getReservation();
        updateDTO = testData.getReservationDto();
        vehicle = testData.getVehicle();
        UserUpdateDTO userDTO = testData.getUserDto();
        VehicleUpdateDTO vehicleDTO = testData.getVehicleDto();
        saveDTO = new ReservationSaveDTO(
                reservation.getStatus().toString(),
                reservation.getStartDatetime().toString(),
                reservation.getEndDatetime().toString(),
                userDTO,
                Set.of(vehicleDTO));
    }

    @Test
    public void testGetAlls() {
        reservationService.findAll();

        verify(reservationRepository).findAll();
    }

    @Test
    public void testSave() {
        vehicle.setReservations(new HashSet<>());
        reservation.setVehicles(new HashSet<>());

        when(reservationRepository.save(any(Reservation.class))).thenReturn(reservation);
        when(vehicleRepository.findById(anyLong())).thenReturn(Optional.of(vehicle));

        ReservationResponseDTO saved = reservationService.save(saveDTO);

        for (Vehicle relativeVehicle : reservation.getVehicles()) {
            verify(vehicleRepository).findById(relativeVehicle.getId());
        }

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSXXX");

        Assertions.assertEquals(reservation.getStatus().toString(), saved.getStatus());
        Assertions.assertEquals(sdf.format(reservation.getStartDatetime()), saved.getStartDatetime());
        Assertions.assertEquals(sdf.format(reservation.getEndDatetime()), saved.getEndDatetime());
        Assertions.assertEquals(reservation.getUser().getId(), saved.getUser().getId());
    }

    @Test
    public void testSave_IllegalArgument() {
        ReservationSaveDTO reservationSaveDTO = new ReservationSaveDTO();
        List<String> wrongStatus = List.of("", "123", "active");
        for (String status: wrongStatus) {
            reservationSaveDTO.setStatus(status);

            Assertions.assertThrows(IllegalArgumentException.class, () -> reservationService.save(reservationSaveDTO));
        }
    }

    @Test
    public void testSave_BadRequest_Status() {
        saveDTO.setStatus(null);

        Assertions.assertThrows(BadRequestException.class, () -> reservationService.save(saveDTO));
    }

    @Test
    public void testSave_BadRequest_User() {
        saveDTO.setUser(null);

        Assertions.assertThrows(BadRequestException.class, () -> reservationService.save(saveDTO));
    }

    @Test
    public void testSave_BadRequest_Date() {
        saveDTO.setEndDatetime("2016-06-22 19:10");
        saveDTO.setStartDatetime("2016-06-23 19:10");

        Assertions.assertThrows(BadRequestException.class, () -> reservationService.save(saveDTO));
    }

    @Test
    public void testSave_RepositoryError() {
        Mockito.doThrow(new RuntimeException()).when(reservationRepository).save(any());

        try {
            reservationService.save(saveDTO);
            Assertions.fail();
        } catch (RuntimeException e) {
            Assertions.assertNull(e.getMessage());
        }
    }

    @Test
    public void testUpdate() throws NotFoundException {
        vehicle.setReservations(new HashSet<>());
        reservation.setVehicles(new HashSet<>());

        Mockito.when(reservationRepository.existsById(anyLong())).thenReturn(true);
        when(vehicleRepository.findById(anyLong())).thenReturn(Optional.of(vehicle));
        when(reservationRepository.findById(anyLong())).thenReturn(Optional.of(reservation));

        reservationService.update(updateDTO);

        verify(reservationRepository).save(any(Reservation.class));
    }

    @Test
    public void testUpdate_BadRequest_Status() {
        Mockito.when(reservationRepository.existsById(anyLong())).thenReturn(true);

        updateDTO.setStatus(null);

        Assertions.assertThrows(BadRequestException.class, () -> reservationService.update(updateDTO));
    }

    @Test
    public void testUpdate_BadRequest_User() {
        Mockito.when(reservationRepository.existsById(anyLong())).thenReturn(true);

        updateDTO.setUser(null);

        Assertions.assertThrows(BadRequestException.class, () -> reservationService.update(updateDTO));
    }

    @Test
    public void testUpdate_BadRequest_Date() {
        Mockito.when(reservationRepository.existsById(anyLong())).thenReturn(true);

        updateDTO.setEndDatetime("2016-06-22 19:10");
        updateDTO.setStartDatetime("2016-06-23 19:10");

        Assertions.assertThrows(BadRequestException.class, () -> reservationService.update(updateDTO));
    }

    @Test
    public void testUpdate_RepositoryError() {
        Mockito.doThrow(new RuntimeException()).when(reservationRepository).save(any());
        Mockito.when(reservationRepository.existsById(anyLong())).thenReturn(true);

        try {
            reservationService.update(updateDTO);
            Assertions.fail();
        } catch (NotFoundException e) {
            Assertions.fail();
        } catch (RuntimeException e) {
            Assertions.assertNull(e.getMessage());
        }
    }


    @Test
    public void testFindById_Success() throws NotFoundException {
        Mockito.when(reservationRepository.findById(reservation.getId())).thenReturn(Optional.of(reservation));
        ReservationResponseDTO findReservation = reservationService.findById(reservation.getId());

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSXXX");

        Assertions.assertEquals(reservation.getStatus().toString(), findReservation.getStatus());
        Assertions.assertEquals(sdf.format(reservation.getStartDatetime()), findReservation.getStartDatetime());
        Assertions.assertEquals(sdf.format(reservation.getEndDatetime()), findReservation.getEndDatetime());
        Assertions.assertEquals(reservation.getUser().getId(), findReservation.getUser().getId());
    }

    @Test
    public void testFindById_NotFound() {
        Mockito.when(reservationRepository.findById(reservation.getId())).thenReturn(Optional.empty());

        Assertions.assertThrows(NotFoundException.class, () -> reservationService.findById(reservation.getId()));
    }

    @Test
    public void testDelete_Success() throws NotFoundException {
        Mockito.when(reservationRepository.existsById(anyLong())).thenReturn(true);
        boolean result = reservationService.delete(reservation.getId());
        Assertions.assertTrue(result);
    }

    @Test
    public void testDelete_NotFound() {
        Mockito.when(reservationRepository.existsById(anyLong())).thenReturn(false);

        Assertions.assertThrows(NotFoundException.class, () -> reservationService.delete(reservation.getId()));
    }
}
