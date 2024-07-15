package dev.elis.controller;

import dev.elis.dto.user.UserResponseDTO;
import dev.elis.dto.user.UserSaveDTO;
import dev.elis.dto.user.UserUpdateDTO;
import dev.elis.exception.NotFoundException;
import dev.elis.service.UserService;
import org.postgresql.util.PSQLException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/user")
public class UserController {
    public static final String INCORRECT_INPUT_MSG = "Incorrect Input";
    public static final String UNABLE_DELETE_MSG = "Unable to delete User, it have related vehicle";
    public static final String CANT_FIND_MSG = "Can't find User. ";

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getById(@PathVariable Long id) {
        try {
            UserResponseDTO userDTO = userService.findById(id);
            return new ResponseEntity<>(userDTO, HttpStatus.OK);
        } catch (NotFoundException e) {
            return new ResponseEntity<>(CANT_FIND_MSG + e.getMessage(), HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>(INCORRECT_INPUT_MSG, HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/all")
    public ResponseEntity<List<UserResponseDTO>> getAlls() {
        List<UserResponseDTO> userResponseDtoList = userService.findAll();
        return new ResponseEntity<>(userResponseDtoList, HttpStatus.OK);
    }

    @PostMapping()
    public ResponseEntity<?> create(@RequestBody UserSaveDTO userDTO) {
        try {
            UserResponseDTO savedUser = userService.save(userDTO);
            return new ResponseEntity<>(savedUser, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(INCORRECT_INPUT_MSG, HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping()
    public ResponseEntity<?> update(@RequestBody UserUpdateDTO userDTO) {
        try {
            userService.update(userDTO);
            return ResponseEntity.ok().build();
        } catch (NotFoundException e) {
            return new ResponseEntity<>(CANT_FIND_MSG + e.getMessage(), HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>(INCORRECT_INPUT_MSG, HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id) {
        try {
            if (userService.delete(id)) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            } else {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        } catch (DataIntegrityViolationException e) {
            Throwable cause = e.getCause() != null ? e.getCause().getCause() : e;
            if (cause instanceof PSQLException) {
                return new ResponseEntity<>(UNABLE_DELETE_MSG, HttpStatus.BAD_REQUEST);
            }
            return new ResponseEntity<>(INCORRECT_INPUT_MSG, HttpStatus.BAD_REQUEST);
        } catch (NotFoundException e) {
            return new ResponseEntity<>(CANT_FIND_MSG + e.getMessage(), HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>(INCORRECT_INPUT_MSG, HttpStatus.BAD_REQUEST);
        }
    }
}