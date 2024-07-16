package dev.elis.controller;

import dev.elis.TestData;
import dev.elis.dto.city.CityResponseDTO;
import dev.elis.dto.city.CitySaveDTO;
import dev.elis.dto.city.CityUpdateDTO;
import dev.elis.exception.NotFoundException;
import dev.elis.model.City;
import dev.elis.service.CityService;
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

import static dev.elis.controller.CityController.*;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CityControllerTest {
    private final CityService cityService = mock(CityService.class);
    private CityController cityController;

    private City city;
    private CitySaveDTO saveDTO;
    private CityUpdateDTO updateDTO;
    private CityResponseDTO responseDTO;

    @BeforeEach
    public void setUp() {
        cityController = new CityController(cityService);

        TestData testData = new TestData();
        city = testData.getCity();
        updateDTO = testData.getCityDto();
        saveDTO = new CitySaveDTO(city.getName());
        responseDTO = new CityResponseDTO(city.getId(), city.getName(), List.of(testData.getVehicleDto()));
    }

    @Test
    public void testGetAlls() throws Exception {
        when(cityService.findAll()).thenReturn(new ArrayList<>());

        ResponseEntity<List<CityResponseDTO>> response = cityController.getAlls();

        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
        Assertions.assertNotNull(response.getBody());
    }

    @Test
    public void testGetById() throws Exception {
        when(cityService.findById(anyLong())).thenReturn(new CityResponseDTO());

        ResponseEntity<?> response = cityController.getById(1L);

        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
        Assertions.assertNotNull(response.getBody());
    }

    @Test
    public void testGetById_NotFound() throws Exception {
        doThrow(new NotFoundException("")).when(cityService).findById(anyLong());

        ResponseEntity<?> response = cityController.getById(1L);
        Assertions.assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        Assertions.assertEquals(CANT_FIND_MSG, response.getBody());

    }

    @Test
    public void testGetById_BadRequest() throws Exception {
        doThrow(RuntimeException.class).when(cityService).findById(anyLong());

        ResponseEntity<?> response = cityController.getById(1L);
        Assertions.assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        Assertions.assertEquals(INCORRECT_INPUT_MSG, response.getBody());
    }

    @Test
    public void testCreate_Success() throws Exception {
        when(cityService.save(any(CitySaveDTO.class))).thenReturn(responseDTO);

        ResponseEntity<?> response = cityController.create(saveDTO);

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
        when(mockPSqlException.getMessage()).thenReturn(city.getName());

        doThrow(mockDIVException).when(cityService).save(any(CitySaveDTO.class));

        ResponseEntity<?> response = cityController.create(saveDTO);
        Assertions.assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        Assertions.assertEquals(NOT_UNIQUE_MSG, response.getBody());
    }

    @Test
    public void testCreate_DataIntegrityViolation() throws Exception {
        doThrow(DataIntegrityViolationException.class).when(cityService).save(any(CitySaveDTO.class));

        ResponseEntity<?> response = cityController.create(saveDTO);
        Assertions.assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        Assertions.assertEquals(INCORRECT_INPUT_MSG, response.getBody());
    }

    @Test
    public void testCreate_BadRequest() throws Exception {
        doThrow(RuntimeException.class).when(cityService).save(any(CitySaveDTO.class));

        ResponseEntity<?> response = cityController.create(saveDTO);
        Assertions.assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        Assertions.assertEquals(INCORRECT_INPUT_MSG, response.getBody());
    }


    @Test
    public void testUpdate_Success() throws Exception {
        ResponseEntity<?> response = cityController.update(updateDTO);
        verify(cityService).update(updateDTO);

        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    public void testUpdate_NotFound() throws Exception {
        doThrow(new NotFoundException("")).when(cityService).update(any(CityUpdateDTO.class));

        ResponseEntity<?> response = cityController.update(updateDTO);
        Assertions.assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        Assertions.assertEquals(CANT_FIND_MSG, response.getBody());

    }

    @Test
    public void testUpdate_BadRequest() throws Exception {
        doThrow(RuntimeException.class).when(cityService).update(any(CityUpdateDTO.class));

        ResponseEntity<?> response = cityController.update(updateDTO);
        Assertions.assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        Assertions.assertEquals(INCORRECT_INPUT_MSG, response.getBody());
    }

    @Test
    public void testDelete_Success() throws Exception {
        when(cityService.delete(1L)).thenReturn(true);
        when(cityService.delete(2L)).thenReturn(false);

        ResponseEntity<?> responseTrue = cityController.delete(1L);

        Assertions.assertEquals(HttpStatus.NO_CONTENT, responseTrue.getStatusCode());

        ResponseEntity<?> responseFalse = cityController.delete(2L);

        Assertions.assertEquals(HttpStatus.NOT_FOUND, responseFalse.getStatusCode());
    }

    @Test
    public void testDelete_UnableDelete() throws Exception {
        Exception mockException = mock(Exception.class);
        Exception mockDIVException = mock(DataIntegrityViolationException.class);

        when(mockDIVException.getCause()).thenReturn(mockException);
        when(mockException.getCause()).thenReturn(mock(PSQLException.class));

        doThrow(mockDIVException).when(cityService).delete(anyLong());

        ResponseEntity<?> response = cityController.delete(1L);
        Assertions.assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        Assertions.assertEquals(UNABLE_DELETE_MSG, response.getBody());
    }

    @Test
    public void testDelete_UnexpectedUnableDelete() throws Exception {
        doThrow(DataIntegrityViolationException.class).when(cityService).delete(anyLong());

        ResponseEntity<?> response = cityController.delete(1L);
        Assertions.assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        Assertions.assertEquals(INCORRECT_INPUT_MSG, response.getBody());
    }

    @Test
    public void testDelete_NotFound() throws Exception {
        doThrow(new NotFoundException("")).when(cityService).delete(anyLong());

        ResponseEntity<?> response = cityController.delete(1L);
        Assertions.assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        Assertions.assertEquals(CANT_FIND_MSG, response.getBody());
    }

    @Test
    public void testDelete_BadRequest() throws Exception {
        doThrow(RuntimeException.class).when(cityService).delete(anyLong());

        ResponseEntity<?> response = cityController.delete(1L);
        Assertions.assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        Assertions.assertEquals(INCORRECT_INPUT_MSG, response.getBody());
    }
}