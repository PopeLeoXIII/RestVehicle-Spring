package dev.elis.controller;

import dev.elis.TestData;
import dev.elis.dto.reservation.ReservationResponseDTO;
import dev.elis.dto.reservation.ReservationSaveDTO;
import dev.elis.dto.reservation.ReservationUpdateDTO;
import dev.elis.dto.user.UserUpdateDTO;
import dev.elis.dto.vehicle.VehicleResponseDTO;
import dev.elis.dto.vehicle.VehicleUpdateDTO;
import dev.elis.exception.NotFoundException;
import dev.elis.model.*;
import dev.elis.service.ReservationService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static dev.elis.controller.ReservationController.*;
import static dev.elis.controller.UserController.INCORRECT_INPUT_MSG;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ReservationControllerTest {
    private final ReservationService reservationService = mock(ReservationService.class);
    private ReservationController reservationController;

    private Reservation reservation;
    private ReservationSaveDTO saveDTO;
    private ReservationUpdateDTO updateDTO;
    private ReservationResponseDTO responseDTO;

    @BeforeEach
    public void setUp() {
        reservationController = new ReservationController(reservationService);

        TestData testData = new TestData();
        reservation = testData.getReservation();
        UserUpdateDTO userDTO = testData.getUserDto();
        VehicleUpdateDTO vehicleDTO = testData.getVehicleDto();
        VehicleResponseDTO vehicleResponse = new VehicleResponseDTO();

        saveDTO = new ReservationSaveDTO(
                reservation.getStatus().toString(),
                reservation.getStartDatetime().toString(),
                reservation.getEndDatetime().toString(),
                userDTO,
                Set.of(vehicleDTO) );
        updateDTO = testData.getReservationDto();
        responseDTO = new ReservationResponseDTO(
                reservation.getId(),
                reservation.getStatus().toString(),
                reservation.getStartDatetime().toString(),
                reservation.getEndDatetime().toString(),
                userDTO,
                Set.of(vehicleResponse));
    }

    @Test
    public void testGetAlls() {
        when(reservationService.findAll()).thenReturn(new ArrayList<>());

        ResponseEntity<List<ReservationResponseDTO>> response = reservationController.getAlls();

        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
        Assertions.assertNotNull(response.getBody());
    }

    @Test
    public void testGetById() throws Exception {
        when(reservationService.findById(anyLong())).thenReturn(new ReservationResponseDTO());

        ResponseEntity<?> response = reservationController.getById(1L);

        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
        Assertions.assertNotNull(response.getBody());
    }

    @Test
    public void testGetById_NotFound() throws Exception {
        doThrow(new NotFoundException("")).when(reservationService).findById(anyLong());

        ResponseEntity<?> response = reservationController.getById(1L);
        Assertions.assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        Assertions.assertEquals(CANT_FIND_MSG, response.getBody());

    }

    @Test
    public void testGetById_BadRequest() throws Exception {
        doThrow(RuntimeException.class).when(reservationService).findById(anyLong());

        ResponseEntity<?> response = reservationController.getById(1L);
        Assertions.assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        Assertions.assertEquals(INCORRECT_INPUT_MSG, response.getBody());
    }

    @Test
    public void testCreate_Success() {
        when(reservationService.save(any(ReservationSaveDTO.class))).thenReturn(responseDTO);

        ResponseEntity<?> response = reservationController.create(saveDTO);

        Assertions.assertEquals(HttpStatus.CREATED, response.getStatusCode());
        Assertions.assertEquals(responseDTO, response.getBody());
    }

    @Test
    public void testCreate_BadRequest() {
        doThrow(RuntimeException.class).when(reservationService).save(any(ReservationSaveDTO.class));

        ResponseEntity<?> response = reservationController.create(saveDTO);
        Assertions.assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        Assertions.assertEquals(INCORRECT_INPUT_MSG, response.getBody());
    }

    @Test
    public void testUpdate_Success() throws Exception {
        ResponseEntity<?> response = reservationController.update(updateDTO);
        verify(reservationService).update(updateDTO);

        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    public void testUpdate_NotFound() throws Exception {
        doThrow(new NotFoundException("")).when(reservationService).update(any(ReservationUpdateDTO.class));

        ResponseEntity<?> response = reservationController.update(updateDTO);
        Assertions.assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        Assertions.assertEquals(CANT_FIND_MSG, response.getBody());

    }

    @Test
    public void testUpdate_BadRequest() throws Exception {
        doThrow(RuntimeException.class).when(reservationService).update(any(ReservationUpdateDTO.class));

        ResponseEntity<?> response = reservationController.update(updateDTO);
        Assertions.assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        Assertions.assertEquals(INCORRECT_INPUT_MSG, response.getBody());
    }

    @Test
    public void testDelete_Success() throws Exception {
        when(reservationService.delete(1L)).thenReturn(true);
        when(reservationService.delete(2L)).thenReturn(false);

        ResponseEntity<?> responseTrue = reservationController.delete(1L);

        Assertions.assertEquals(HttpStatus.NO_CONTENT, responseTrue.getStatusCode());

        ResponseEntity<?> responseFalse = reservationController.delete(2L);

        Assertions.assertEquals(HttpStatus.NOT_FOUND, responseFalse.getStatusCode());
    }

    @Test
    public void testDelete_NotFound() throws Exception {
        doThrow(new NotFoundException("")).when(reservationService).delete(anyLong());

        ResponseEntity<?> response = reservationController.delete(1L);
        Assertions.assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        Assertions.assertEquals(CANT_FIND_MSG, response.getBody());
    }

    @Test
    public void testDelete_BadRequest() throws Exception {
        doThrow(RuntimeException.class).when(reservationService).delete(anyLong());

        ResponseEntity<?> response = reservationController.delete(1L);
        Assertions.assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        Assertions.assertEquals(INCORRECT_INPUT_MSG, response.getBody());
    }
}