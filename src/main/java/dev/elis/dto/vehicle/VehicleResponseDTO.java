package dev.elis.dto.vehicle;


import dev.elis.dto.city.CityResponseDTO;
import dev.elis.dto.reservation.ReservationResponseDTO;

import java.util.List;

public class VehicleResponseDTO {
    private Long id;
    private String name;
    private CityResponseDTO city;
    private List<ReservationResponseDTO> reservationList;

    public VehicleResponseDTO() {}

    public VehicleResponseDTO(Long id, String name, CityResponseDTO city, List<ReservationResponseDTO> reservationList) {
        this.id = id;
        this.name = name;
        this.city = city;
        this.reservationList = reservationList;
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

    public CityResponseDTO getCity() {
        return city;
    }

    public void setCity(CityResponseDTO city) {
        this.city = city;
    }

    public List<ReservationResponseDTO> getReservationList() {
        return reservationList;
    }

    public void setReservationList(List<ReservationResponseDTO> reservationList) {
        this.reservationList = reservationList;
    }
}
