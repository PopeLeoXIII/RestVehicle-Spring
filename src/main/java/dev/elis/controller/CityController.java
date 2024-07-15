package dev.elis.controller;

import dev.elis.dto.city.*;
import dev.elis.exception.NotFoundException;
import dev.elis.service.CityService;
import org.postgresql.util.PSQLException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/city")
public class CityController {
    public static final String INCORRECT_INPUT_MSG = "Incorrect Input";
    public static final String UNABLE_DELETE_MSG = "Unable to delete City, it have related vehicle";
    public static final String NOT_UNIQUE_MSG = "Unable to insert City, this name is already exist";
    public static final String CANT_FIND_MSG = "Can't find City. ";

    private final CityService cityService;

    @Autowired
    public CityController(CityService cityService) {
        this.cityService = cityService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getById(@PathVariable Long id) {
        try {
            CityResponseDTO cityResponseDTO = cityService.findById(id);
            return new ResponseEntity<>(cityResponseDTO, HttpStatus.OK);
        } catch (NotFoundException e) {
            return new ResponseEntity<>(CANT_FIND_MSG + e.getMessage(), HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>(INCORRECT_INPUT_MSG, HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/all")
    public ResponseEntity<List<CityResponseDTO>> getAlls() {
        List<CityResponseDTO> cityResponseDtoList = cityService.findAll();
        return new ResponseEntity<>(cityResponseDtoList, HttpStatus.OK);
    }

    @PostMapping()
    public ResponseEntity<?> create(@RequestBody CitySaveDTO saveDTO) {
        try {
            CityResponseDTO savedCity = cityService.save(saveDTO);
            return new ResponseEntity<>(savedCity, HttpStatus.CREATED);
        } catch (DataIntegrityViolationException e) {
            Throwable cause = e.getCause() != null ? e.getCause().getCause() : e;
            if (cause instanceof PSQLException && cause.getMessage().contains(saveDTO.getName())) {
                return new ResponseEntity<>(NOT_UNIQUE_MSG, HttpStatus.BAD_REQUEST);
            }
            return new ResponseEntity<>(INCORRECT_INPUT_MSG, HttpStatus.BAD_REQUEST);
        }catch (Exception e) {
            return new ResponseEntity<>(INCORRECT_INPUT_MSG, HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping()
    public ResponseEntity<?> update(@RequestBody CityUpdateDTO cityUpdateDTO) {
        try {
            cityService.update(cityUpdateDTO);
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
            if (cityService.delete(id)) {
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