package dev.elis.dto.reservation;

import dev.elis.dto.user.UserResponseDTO;
import dev.elis.dto.vehicle.VehicleResponseDTO;

import java.util.List;

public class ReservationResponseDTO {
    private Long id;
    private String status;
    private String startDatetime;
    private String endDatetime;
    private List<VehicleResponseDTO> vehicleList;
    private UserResponseDTO user;

    public ReservationResponseDTO() {}

    public ReservationResponseDTO(Long id, String status, String startDatetime, String endDatetime, List<VehicleResponseDTO> vehicleList, UserResponseDTO user) {
        this.id = id;
        this.status = status;
        this.startDatetime = startDatetime;
        this.endDatetime = endDatetime;
        this.vehicleList = vehicleList;
        this.user = user;
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

    public List<VehicleResponseDTO> getVehicleList() {
        return vehicleList;
    }

    public void setVehicleList(List<VehicleResponseDTO> vehicleList) {
        this.vehicleList = vehicleList;
    }

    public UserResponseDTO getUser() {
        return user;
    }

    public void setUser(UserResponseDTO user) {
        this.user = user;
    }
}
