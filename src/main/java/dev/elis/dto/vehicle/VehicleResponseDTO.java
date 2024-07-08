package dev.elis.dto.vehicle;

import dev.elis.dto.city.CityUpdateDTO;
import dev.elis.dto.reservation.ReservationResponseDTO;

import java.util.List;
import java.util.Set;

public class VehicleResponseDTO {
    private Long id;
    private String name;
    private CityUpdateDTO city;
    private Set<ReservationResponseDTO> reservations;

    public VehicleResponseDTO() {}

    public VehicleResponseDTO(Long id, String name, CityUpdateDTO city, Set<ReservationResponseDTO> reservations) {
        this.id = id;
        this.name = name;
        this.city = city;
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

    public CityUpdateDTO getCity() {
        return city;
    }

    public void setCity(CityUpdateDTO city) {
        this.city = city;
    }

    public Set<ReservationResponseDTO> getReservations() {
        return reservations;
    }

    public void setReservations(Set<ReservationResponseDTO> reservations) {
        this.reservations = reservations;
    }
}
