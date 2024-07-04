package dev.elis.controller;

import dev.elis.dto.reservation.ReservationResponseDTO;
import dev.elis.dto.reservation.ReservationSaveDTO;
import dev.elis.dto.reservation.ReservationUpdateDTO;
import dev.elis.exception.NotFoundException;
import dev.elis.service.ReservationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/reservation")
public class ReservationController {

    private final ReservationService reservationService;

    @Autowired
    public ReservationController(ReservationService reservationService) {
        this.reservationService = reservationService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getCustomerById(@PathVariable Long id) {
        try {
            ReservationResponseDTO reservationResponseDTO = reservationService.findById(id);
            return new ResponseEntity<>(reservationResponseDTO, HttpStatus.OK);
        } catch (NotFoundException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/all")
    public ResponseEntity<List<ReservationResponseDTO>> getAllCustomers() {
        List<ReservationResponseDTO> reservationResponseDtoList = reservationService.findAll();
        return new ResponseEntity<>(reservationResponseDtoList, HttpStatus.OK);
    }

    @PostMapping()
    public ResponseEntity<?> createCustomer(@RequestBody ReservationSaveDTO reservationSaveDTO) {
        try {
            ReservationResponseDTO savedReservation = reservationService.save(reservationSaveDTO);
            return new ResponseEntity<>(savedReservation, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>("Incorrect Input...", HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping()
    public ResponseEntity<?> updateCustomer(@RequestBody ReservationUpdateDTO reservationUpdateDTO) {
        try {
            reservationService.update(reservationUpdateDTO);
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
            if (reservationService.delete(id)) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            } else {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        } catch (NotFoundException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }
}