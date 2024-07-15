package dev.elis.dto.user;

import dev.elis.dto.reservation.ReservationResponseDTO;
import dev.elis.dto.reservation.ReservationUpdateDTO;

import java.util.List;

public class UserResponseDTO {
    private Long id;
    private String name;
    private String surname;
    private List<ReservationUpdateDTO> reservations;

    public UserResponseDTO() {}

    public UserResponseDTO(Long id, String name, String surname, List<ReservationUpdateDTO> reservations) {
        this.id = id;
        this.name = name;
        this.surname = surname;
        this.reservations = reservations;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public List<ReservationUpdateDTO> getReservations() {
        return reservations;
    }

    public void setReservations(List<ReservationUpdateDTO> reservations) {
        this.reservations = reservations;
    }
}
