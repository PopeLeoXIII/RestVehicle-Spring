package dev.elis.controller;

import dev.elis.dto.reservation.ReservationResponseDTO;
import dev.elis.dto.reservation.ReservationSaveDTO;
import dev.elis.dto.reservation.ReservationUpdateDTO;
import dev.elis.exception.NotFoundException;
import dev.elis.service.ReservationService;
import org.postgresql.util.PSQLException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/reservation")
public class ReservationController {
    public static final String INCORRECT_INPUT_MSG = "Incorrect Input";
    public static final String UNABLE_DELETE_MSG = "Unable to delete Reservation, it have related vehicle";

    private final ReservationService reservationService;

    @Autowired
    public ReservationController(ReservationService reservationService) {
        this.reservationService = reservationService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getById(@PathVariable Long id) {
        try {
            ReservationResponseDTO reservationDTO = reservationService.findById(id);
            return new ResponseEntity<>(reservationDTO, HttpStatus.OK);
        } catch (NotFoundException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>(INCORRECT_INPUT_MSG, HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/all")
    public ResponseEntity<List<ReservationResponseDTO>> getAlls() {
        List<ReservationResponseDTO> reservationResponseDtoList = reservationService.findAll();
        return new ResponseEntity<>(reservationResponseDtoList, HttpStatus.OK);
    }

    @PostMapping()
    public ResponseEntity<?> create(@RequestBody ReservationSaveDTO reservationDTO) {
        try {
            ReservationResponseDTO savedReservation = reservationService.save(reservationDTO);
            return new ResponseEntity<>(savedReservation, HttpStatus.CREATED);
        }catch (Exception e) {
            return new ResponseEntity<>(INCORRECT_INPUT_MSG, HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping()
    public ResponseEntity<?> update(@RequestBody ReservationUpdateDTO reservationDTO) {
        try {
            reservationService.update(reservationDTO);
            return ResponseEntity.ok().build();
        } catch (NotFoundException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>(INCORRECT_INPUT_MSG, HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id) {
        try {
            if (reservationService.delete(id)) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            } else {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        } catch (DataIntegrityViolationException e) {
            if (e.getCause().getCause() instanceof PSQLException) {
                return new ResponseEntity<>(UNABLE_DELETE_MSG, HttpStatus.BAD_REQUEST);
            }
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (NotFoundException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>(INCORRECT_INPUT_MSG, HttpStatus.BAD_REQUEST);
        }
    }
}