package dev.elis.controller;

import dev.elis.dto.city.CityUpdateDTO;
import dev.elis.dto.vehicle.VehicleResponseDTO;
import dev.elis.dto.vehicle.VehicleSaveDTO;
import dev.elis.dto.vehicle.VehicleUpdateDTO;
import dev.elis.dto.vehicle.VehicleUpdateDTO;
import dev.elis.exception.NotFoundException;
import dev.elis.model.City;
import dev.elis.model.Vehicle;
import dev.elis.model.Vehicle;
import dev.elis.service.VehicleService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.postgresql.util.PSQLException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static dev.elis.controller.VehicleController.*;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class VehicleControllerTest {
    private final VehicleService vehicleService = mock(VehicleService.class);
    private VehicleController vehicleController;

    private Vehicle vehicle;
    private VehicleSaveDTO saveDTO;
    private VehicleUpdateDTO updateDTO;
    private VehicleResponseDTO responseDTO;
    private City city;
    private CityUpdateDTO cityDTO;

    @BeforeEach
    public void setUp() {
        vehicleController = new VehicleController(vehicleService);
        city = new City(3L, "City", List.of());
        cityDTO = new CityUpdateDTO(city.getId(), city.getName());
        vehicle = new Vehicle(1L, "Vehicle", city, Set.of());
        saveDTO = new VehicleSaveDTO(vehicle.getName(), cityDTO);
        updateDTO = new VehicleUpdateDTO(vehicle.getId(), vehicle.getName(), cityDTO);
        responseDTO = new VehicleResponseDTO(vehicle.getId(), vehicle.getName(), cityDTO, Set.of());
    }

    @Test
    public void testGetAlls() throws Exception {
        when(vehicleService.findAll()).thenReturn(new ArrayList<>());

        ResponseEntity<List<VehicleResponseDTO>> response = vehicleController.getAlls();

        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
        Assertions.assertNotNull(response.getBody());
    }

    @Test
    public void testGetById() throws Exception {
        when(vehicleService.findById(anyLong())).thenReturn(new VehicleResponseDTO());

        ResponseEntity<?> response = vehicleController.getById(1L);

        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
        Assertions.assertNotNull(response.getBody());
    }

    @Test
    public void testGetById_NotFound() throws Exception {
        doThrow(new NotFoundException("")).when(vehicleService).findById(anyLong());

        ResponseEntity<?> response = vehicleController.getById(1L);
        Assertions.assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        Assertions.assertEquals(CANT_FIND_MSG, response.getBody());

    }

    @Test
    public void testGetById_BadRequest() throws Exception {
        doThrow(RuntimeException.class).when(vehicleService).findById(anyLong());

        ResponseEntity<?> response = vehicleController.getById(1L);
        Assertions.assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        Assertions.assertEquals(INCORRECT_INPUT_MSG, response.getBody());
    }

    @Test
    public void testCreate_Success() throws Exception {
        when(vehicleService.save(any(VehicleSaveDTO.class))).thenReturn(responseDTO);

        ResponseEntity<?> response = vehicleController.create(saveDTO);

        Assertions.assertEquals(HttpStatus.CREATED, response.getStatusCode());
        Assertions.assertEquals(responseDTO, response.getBody());
    }

    @Test
    public void testCreate_NotUnique() throws Exception {
        Exception mockException = mock(Exception.class);
        Exception mockPSqlException = mock(PSQLException.class);
        Exception mockDIVException = mock(DataIntegrityViolationException.class);

        when(mockDIVException.getCause()).thenReturn(mockException);
        when(mockException.getCause()).thenReturn(mockPSqlException);
        when(mockPSqlException.getMessage()).thenReturn(vehicle.getName());

        doThrow(mockDIVException).when(vehicleService).save(any(VehicleSaveDTO.class));

        ResponseEntity<?> response = vehicleController.create(saveDTO);
        Assertions.assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        Assertions.assertEquals(NOT_UNIQUE_MSG, response.getBody());
    }

    @Test
    public void testCreate_DataIntegrityViolation() throws Exception {
        doThrow(DataIntegrityViolationException.class).when(vehicleService).save(any(VehicleSaveDTO.class));

        ResponseEntity<?> response = vehicleController.create(saveDTO);
        Assertions.assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        Assertions.assertEquals(INCORRECT_INPUT_MSG, response.getBody());
    }

    @Test
    public void testCreate_BadRequest() throws Exception {
        doThrow(RuntimeException.class).when(vehicleService).save(any(VehicleSaveDTO.class));

        ResponseEntity<?> response = vehicleController.create(saveDTO);
        Assertions.assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        Assertions.assertEquals(INCORRECT_INPUT_MSG, response.getBody());
    }

    @Test
    public void testUpdate_Success() throws Exception {
        ResponseEntity<?> response = vehicleController.update(updateDTO);
        verify(vehicleService).update(updateDTO);

        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    public void testUpdate_NotFound() throws Exception {
        doThrow(new NotFoundException("")).when(vehicleService).update(any(VehicleUpdateDTO.class));

        ResponseEntity<?> response = vehicleController.update(updateDTO);
        Assertions.assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        Assertions.assertEquals(CANT_FIND_MSG, response.getBody());

    }

    @Test
    public void testUpdate_BadRequest() throws Exception {
        doThrow(RuntimeException.class).when(vehicleService).update(any(VehicleUpdateDTO.class));

        ResponseEntity<?> response = vehicleController.update(updateDTO);
        Assertions.assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        Assertions.assertEquals(INCORRECT_INPUT_MSG, response.getBody());
    }

    @Test
    public void testDelete_Success() throws Exception {
        when(vehicleService.delete(1L)).thenReturn(true);
        when(vehicleService.delete(2L)).thenReturn(false);

        ResponseEntity<?> responseTrue = vehicleController.delete(1L);

        Assertions.assertEquals(HttpStatus.NO_CONTENT, responseTrue.getStatusCode());

        ResponseEntity<?> responseFalse = vehicleController.delete(2L);

        Assertions.assertEquals(HttpStatus.NOT_FOUND, responseFalse.getStatusCode());
    }

    @Test
    public void testDelete_NotFound() throws Exception {
        doThrow(new NotFoundException("")).when(vehicleService).delete(anyLong());

        ResponseEntity<?> response = vehicleController.delete(1L);
        Assertions.assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        Assertions.assertEquals(CANT_FIND_MSG, response.getBody());
    }

    @Test
    public void testDelete_BadRequest() throws Exception {
        doThrow(RuntimeException.class).when(vehicleService).delete(anyLong());

        ResponseEntity<?> response = vehicleController.delete(1L);
        Assertions.assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        Assertions.assertEquals(INCORRECT_INPUT_MSG, response.getBody());
    }
}