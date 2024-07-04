package dev.elis.service;

import dev.elis.dto.reservation.ReservationResponseDTO;
import dev.elis.dto.reservation.ReservationSaveDTO;
import dev.elis.dto.reservation.ReservationUpdateDTO;
import dev.elis.exception.NotFoundException;
import dev.elis.mapper.ReservationDTOMapper;
import dev.elis.model.Reservation;
import dev.elis.repository.ReservationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ReservationService {

    private final ReservationRepository reservationRepository;
    private final ReservationDTOMapper reservationDTOMapper;

    @Autowired
    public ReservationService(ReservationRepository reservationRepository, ReservationDTOMapper reservationDTOMapper) {
        this.reservationRepository = reservationRepository;
        this.reservationDTOMapper = reservationDTOMapper;
    }

    @Transactional
    public ReservationResponseDTO save(ReservationSaveDTO reservationSaveDTO) {
        Reservation reservation = reservationDTOMapper.toEntityInc(reservationSaveDTO);
        reservation = reservationRepository.save(reservation);
        return reservationDTOMapper.toDTO(reservation);
    }

    @Transactional
    public void update(ReservationUpdateDTO reservationUpdateDTO) throws NotFoundException {
        checkCustomerExist(reservationUpdateDTO.getId());
        Reservation reservation = reservationDTOMapper.toEntityUpd(reservationUpdateDTO);
        reservationRepository.save(reservation);
    }

    public ReservationResponseDTO findById(Long id) throws NotFoundException {
        Reservation reservation = reservationRepository.findById(id).orElseThrow(() ->
                new NotFoundException("This Customer does not exist!"));
        return reservationDTOMapper.toDTO(reservation);
    }

    public List<ReservationResponseDTO> findAll() {
        List<Reservation> cities = reservationRepository.findAll();
        return cities.stream()
                .map(reservationDTOMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Transactional
    public boolean delete(Long id) throws NotFoundException {
        checkCustomerExist(id);
        reservationRepository.deleteById(id);
        return true;
    }

    private void checkCustomerExist(Long id) throws NotFoundException {
        if (!reservationRepository.existsById(id)) {
            throw new NotFoundException("This Customer does not exist!");
        }
    }
}