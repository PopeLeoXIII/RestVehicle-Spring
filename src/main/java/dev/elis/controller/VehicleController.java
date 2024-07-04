package dev.elis.controller;

import dev.elis.dto.vehicle.VehicleResponseDTO;
import dev.elis.dto.vehicle.VehicleSaveDTO;
import dev.elis.dto.vehicle.VehicleUpdateDTO;
import dev.elis.exception.NotFoundException;
import dev.elis.service.VehicleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/vehicle")
public class VehicleController {

    private final VehicleService vehicleService;

    @Autowired
    public VehicleController(VehicleService vehicleService) {
        this.vehicleService = vehicleService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getCustomerById(@PathVariable Long id) {
        try {
            VehicleResponseDTO vehicleResponseDTO = vehicleService.findById(id);
            return new ResponseEntity<>(vehicleResponseDTO, HttpStatus.OK);
        } catch (NotFoundException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/all")
    public ResponseEntity<List<VehicleResponseDTO>> getAllCustomers() {
        List<VehicleResponseDTO> vehicleResponseDtoList = vehicleService.findAll();
        return new ResponseEntity<>(vehicleResponseDtoList, HttpStatus.OK);
    }

    @PostMapping()
    public ResponseEntity<?> createCustomer(@RequestBody VehicleSaveDTO vehicleSaveDTO) {
        try {
            VehicleResponseDTO savedVehicle = vehicleService.save(vehicleSaveDTO);
            return new ResponseEntity<>(savedVehicle, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>("Incorrect Input...", HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping()
    public ResponseEntity<?> updateCustomer(@RequestBody VehicleUpdateDTO vehicleUpdateDTO) {
        try {
            vehicleService.update(vehicleUpdateDTO);
            return ResponseEntity.ok().build();
        } catch (NotFoundException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>("Incorrect Input...", HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteCustomer(@PathVariable Long id) {
        try {
            if (vehicleService.delete(id)) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            } else {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        } catch (NotFoundException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }
}