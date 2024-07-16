package dev.elis.service;

import dev.elis.TestData;
import dev.elis.dto.user.UserResponseDTO;
import dev.elis.dto.user.UserSaveDTO;
import dev.elis.dto.user.UserUpdateDTO;
import dev.elis.exception.BadRequestException;
import dev.elis.exception.NotFoundException;
import dev.elis.mapper.UserDTOMapperImpl;
import dev.elis.model.User;
import dev.elis.repository.UserRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {
    private final UserRepository userRepository = mock(UserRepository.class);
    private final UserDTOMapperImpl userDTOMapper = new UserDTOMapperImpl();
    private UserService userService;

    private User user;
    private UserSaveDTO saveDTO;
    private UserUpdateDTO updateDTO;

    @BeforeEach
    public void setUp() {
        userService = new UserService(userRepository, userDTOMapper);

        TestData testData = new TestData();
        user = testData.getUser();
        updateDTO = testData.getUserDto();
        saveDTO = new UserSaveDTO(user.getName(), user.getSurname());
    }

    @Test
    public void testGetAlls() {
        userService.findAll();

        verify(userRepository).findAll();
    }

    @Test
    public void testSave() {
        when(userRepository.save(any(User.class))).thenReturn(user);

        userService.save(saveDTO);

        Assertions.assertEquals(user.getName(), saveDTO.getName());
    }

    @Test
    public void testSave_BadRequest_Name() {
        UserSaveDTO userSaveDTO = new UserSaveDTO();
        userSaveDTO.setName("");

        Assertions.assertThrows(BadRequestException.class, () -> userService.save(userSaveDTO));
    }

    @Test
    public void testSave_BadRequest_Surname() {
        UserSaveDTO userSaveDTO = new UserSaveDTO();
        userSaveDTO.setSurname("");

        Assertions.assertThrows(BadRequestException.class, () -> userService.save(userSaveDTO));
    }

    @Test
    public void testSave_RepositoryError() {
        Mockito.doThrow(new RuntimeException()).when(userRepository).save(any());

        try {
            userService.save(saveDTO);
            Assertions.fail();
        } catch (RuntimeException e) {
            Assertions.assertNull(e.getMessage());
        }
    }

    @Test
    public void testUpdate() throws NotFoundException {
        Mockito.when(userRepository.existsById(anyLong())).thenReturn(true);

        userService.update(updateDTO);

        verify(userRepository).save(any(User.class));
    }

    @Test
    public void testUpdate_BadRequest_Name() {
        UserUpdateDTO userUpdateDTO = new UserUpdateDTO(4L, null, "surname");
        Mockito.when(userRepository.existsById(userUpdateDTO.getId())).thenReturn(true);

        Assertions.assertThrows(BadRequestException.class, () -> userService.update(userUpdateDTO));
    }

    @Test
    public void testUpdate_BadRequest_Surname() {
        UserUpdateDTO userUpdateDTO = new UserUpdateDTO(4L, "null", null);
        Mockito.when(userRepository.existsById(userUpdateDTO.getId())).thenReturn(true);

        Assertions.assertThrows(BadRequestException.class, () -> userService.update(userUpdateDTO));
    }

    @Test
    public void testUpdate_RepositoryError() {
        Mockito.doThrow(new RuntimeException()).when(userRepository).save(any());
        Mockito.when(userRepository.existsById(anyLong())).thenReturn(true);

        try {
            userService.update(updateDTO);
            Assertions.fail();
        } catch (NotFoundException e) {
            Assertions.fail();
        } catch (RuntimeException e) {
            Assertions.assertNull(e.getMessage());
        }
    }


    @Test
    public void testFindById_Success() throws NotFoundException {
        Mockito.when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));
        UserResponseDTO findUser = userService.findById(user.getId());

        Assertions.assertEquals(user.getId(), findUser.getId());
        Assertions.assertEquals(user.getName(), findUser.getName());
    }

    @Test
    public void testFindById_NotFound() {
        Mockito.when(userRepository.findById(user.getId())).thenReturn(Optional.empty());

        Assertions.assertThrows(NotFoundException.class, () -> userService.findById(user.getId()));
    }

    @Test
    public void testDelete_Success() throws NotFoundException {
        Mockito.when(userRepository.existsById(anyLong())).thenReturn(true);
        boolean result = userService.delete(user.getId());
        Assertions.assertTrue(result);
    }

    @Test
    public void testDelete_NotFound() {
        Mockito.when(userRepository.existsById(anyLong())).thenReturn(false);

        Assertions.assertThrows(NotFoundException.class, () -> userService.delete(user.getId()));
    }
}
