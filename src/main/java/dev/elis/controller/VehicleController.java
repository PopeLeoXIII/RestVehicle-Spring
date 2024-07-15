package dev.elis.controller;

import dev.elis.dto.vehicle.VehicleResponseDTO;
import dev.elis.dto.vehicle.VehicleSaveDTO;
import dev.elis.dto.vehicle.VehicleUpdateDTO;
import dev.elis.exception.NotFoundException;
import dev.elis.service.VehicleService;
import org.postgresql.util.PSQLException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/vehicle")
public class VehicleController {
    public static final String INCORRECT_INPUT_MSG = "Incorrect Input";
    public static final String NOT_UNIQUE_MSG = "Unable to insert Vehicle, this name is already exist";
    public static final String CANT_FIND_MSG = "Can't find Vehicle. ";

    private final VehicleService vehicleService;

    @Autowired
    public VehicleController(VehicleService vehicleService) {
        this.vehicleService = vehicleService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getById(@PathVariable Long id) {
        try {
            VehicleResponseDTO vehicleResponseDTO = vehicleService.findById(id);
            return new ResponseEntity<>(vehicleResponseDTO, HttpStatus.OK);
        }  catch (NotFoundException e) {
            return new ResponseEntity<>(CANT_FIND_MSG + e.getMessage(), HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>(INCORRECT_INPUT_MSG, HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/all")
    public ResponseEntity<List<VehicleResponseDTO>> getAlls() {
        List<VehicleResponseDTO> vehicleResponseDtoList = vehicleService.findAll();
        return new ResponseEntity<>(vehicleResponseDtoList, HttpStatus.OK);
    }

    @PostMapping()
    public ResponseEntity<?> create(@RequestBody VehicleSaveDTO vehicleDTO) {
        try {
            VehicleResponseDTO savedVehicle = vehicleService.save(vehicleDTO);
            return new ResponseEntity<>(savedVehicle, HttpStatus.CREATED);
        } catch (DataIntegrityViolationException e) {
            Throwable cause = e.getCause() != null ? e.getCause().getCause() : e;
            if (cause instanceof PSQLException && cause.getMessage().contains(vehicleDTO.getName())) {
                return new ResponseEntity<>(NOT_UNIQUE_MSG, HttpStatus.BAD_REQUEST);
            }
            return new ResponseEntity<>(INCORRECT_INPUT_MSG, HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return new ResponseEntity<>(INCORRECT_INPUT_MSG, HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping()
    public ResponseEntity<?> update(@RequestBody VehicleUpdateDTO vehicleDTO) {
        try {
            vehicleService.update(vehicleDTO);
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
            if (vehicleService.delete(id)) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            } else {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        } catch (NotFoundException e) {
            return new ResponseEntity<>(CANT_FIND_MSG + e.getMessage(), HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>(INCORRECT_INPUT_MSG, HttpStatus.BAD_REQUEST);
        }
    }
}