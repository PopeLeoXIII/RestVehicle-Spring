package dev.elis.controller;

import dev.elis.dto.reservation.ReservationResponseDTO;
import dev.elis.dto.reservation.ReservationUpdateDTO;
import dev.elis.dto.user.UserResponseDTO;
import dev.elis.dto.user.UserSaveDTO;
import dev.elis.dto.user.UserUpdateDTO;
import dev.elis.dto.vehicle.VehicleUpdateDTO;
import dev.elis.exception.NotFoundException;
import dev.elis.model.Reservation;
import dev.elis.model.User;
import dev.elis.service.UserService;
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

import static dev.elis.controller.UserController.*;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserControllerTest {
    private final UserService userService = mock(UserService.class);
    private UserController userController;

    private UserSaveDTO saveDTO;
    private UserUpdateDTO updateDTO;
    private UserResponseDTO responseDTO;

    @BeforeEach
    public void setUp() {
        userController = new UserController(userService);

        TestData testData = new TestData();
        User user = testData.getUser();
        updateDTO = testData.getUserDto();
        saveDTO = new UserSaveDTO(user.getName(), user.getSurname());
        responseDTO = new UserResponseDTO(user.getId(), user.getName(), user.getSurname(), List.of(testData.getReservationDto()));
    }

    @Test
    public void testGetAlls() throws Exception {
        when(userService.findAll()).thenReturn(new ArrayList<>());

        ResponseEntity<List<UserResponseDTO>> response = userController.getAlls();

        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
        Assertions.assertNotNull(response.getBody());
    }

    @Test
    public void testGetById() throws Exception {
        when(userService.findById(anyLong())).thenReturn(new UserResponseDTO());

        ResponseEntity<?> response = userController.getById(1L);

        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
        Assertions.assertNotNull(response.getBody());
    }

    @Test
    public void testGetById_NotFound() throws Exception {
        doThrow(new NotFoundException("")).when(userService).findById(anyLong());

        ResponseEntity<?> response = userController.getById(1L);
        Assertions.assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        Assertions.assertEquals(CANT_FIND_MSG, response.getBody());

    }

    @Test
    public void testGetById_BadRequest() throws Exception {
        doThrow(RuntimeException.class).when(userService).findById(anyLong());

        ResponseEntity<?> response = userController.getById(1L);
        Assertions.assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        Assertions.assertEquals(INCORRECT_INPUT_MSG, response.getBody());
    }

    @Test
    public void testCreate_Success() throws Exception {
        when(userService.save(any(UserSaveDTO.class))).thenReturn(responseDTO);

        ResponseEntity<?> response = userController.create(saveDTO);

        Assertions.assertEquals(HttpStatus.CREATED, response.getStatusCode());
        Assertions.assertEquals(responseDTO, response.getBody());
    }

    @Test
    public void testCreate_BadRequest() throws Exception {
        doThrow(RuntimeException.class).when(userService).save(any(UserSaveDTO.class));

        ResponseEntity<?> response = userController.create(saveDTO);
        Assertions.assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        Assertions.assertEquals(INCORRECT_INPUT_MSG, response.getBody());
    }


    @Test
    public void testUpdate_Success() throws Exception {
        ResponseEntity<?> response = userController.update(updateDTO);
        verify(userService).update(updateDTO);

        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    public void testUpdate_NotFound() throws Exception {
        doThrow(new NotFoundException("")).when(userService).update(any(UserUpdateDTO.class));

        ResponseEntity<?> response = userController.update(updateDTO);
        Assertions.assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        Assertions.assertEquals(CANT_FIND_MSG, response.getBody());

    }

    @Test
    public void testUpdate_BadRequest() throws Exception {
        doThrow(RuntimeException.class).when(userService).update(any(UserUpdateDTO.class));

        ResponseEntity<?> response = userController.update(updateDTO);
        Assertions.assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        Assertions.assertEquals(INCORRECT_INPUT_MSG, response.getBody());
    }

    @Test
    public void testDelete_Success() throws Exception {
        when(userService.delete(1L)).thenReturn(true);
        when(userService.delete(2L)).thenReturn(false);

        ResponseEntity<?> responseTrue = userController.delete(1L);

        Assertions.assertEquals(HttpStatus.NO_CONTENT, responseTrue.getStatusCode());

        ResponseEntity<?> responseFalse = userController.delete(2L);

        Assertions.assertEquals(HttpStatus.NOT_FOUND, responseFalse.getStatusCode());
    }

    @Test
    public void testDelete_UnableDelete() throws Exception {
        Exception mockException = mock(Exception.class);
        Exception mockDIVException = mock(DataIntegrityViolationException.class);

        when(mockDIVException.getCause()).thenReturn(mockException);
        when(mockException.getCause()).thenReturn(mock(PSQLException.class));

        doThrow(mockDIVException).when(userService).delete(anyLong());

        ResponseEntity<?> response = userController.delete(1L);
        Assertions.assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        Assertions.assertEquals(UNABLE_DELETE_MSG, response.getBody());
    }

    @Test
    public void testDelete_UnexpectedUnableDelete() throws Exception {
        doThrow(DataIntegrityViolationException.class).when(userService).delete(anyLong());

        ResponseEntity<?> response = userController.delete(1L);
        Assertions.assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        Assertions.assertEquals(INCORRECT_INPUT_MSG, response.getBody());
    }

    @Test
    public void testDelete_NotFound() throws Exception {
        doThrow(new NotFoundException("")).when(userService).delete(anyLong());

        ResponseEntity<?> response = userController.delete(1L);
        Assertions.assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        Assertions.assertEquals(CANT_FIND_MSG, response.getBody());
    }

    @Test
    public void testDelete_BadRequest() throws Exception {
        doThrow(RuntimeException.class).when(userService).delete(anyLong());

        ResponseEntity<?> response = userController.delete(1L);
        Assertions.assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        Assertions.assertEquals(INCORRECT_INPUT_MSG, response.getBody());
    }
}