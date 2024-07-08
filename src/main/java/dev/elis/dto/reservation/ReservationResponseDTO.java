package dev.elis.dto.reservation;

import dev.elis.dto.user.UserResponseDTO;
import dev.elis.dto.user.UserUpdateDTO;
import dev.elis.dto.vehicle.VehicleResponseDTO;

import java.util.List;
import java.util.Set;

public class ReservationResponseDTO {
    private Long id;
    private String status;
    private String startDatetime;
    private String endDatetime;
    private UserUpdateDTO user;
    private Set<VehicleResponseDTO> vehicles;

    public ReservationResponseDTO() {}

    public ReservationResponseDTO(Long id, String status, String startDatetime, String endDatetime, UserUpdateDTO user, Set<VehicleResponseDTO> vehicles) {
        this.id = id;
        this.status = status;
        this.startDatetime = startDatetime;
        this.endDatetime = endDatetime;
        this.user = user;
        this.vehicles = vehicles;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getStartDatetime() {
        return startDatetime;
    }

    public void setStartDatetime(String startDatetime) {
        this.startDatetime = startDatetime;
    }

    public String getEndDatetime() {
        return endDatetime;
    }

    public void setEndDatetime(String endDatetime) {
        this.endDatetime = endDatetime;
    }

    public Set<VehicleResponseDTO> getVehicles() {
        return vehicles;
    }

    public void setVehicles(Set<VehicleResponseDTO> vehicles) {
        this.vehicles = vehicles;
    }

    public UserUpdateDTO getUser() {
        return user;
    }

    public void setUser(UserUpdateDTO user) {
        this.user = user;
    }
}
