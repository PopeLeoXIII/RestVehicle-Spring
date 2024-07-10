package dev.elis.dto.reservation;

import dev.elis.dto.user.UserUpdateDTO;
import dev.elis.dto.vehicle.VehicleUpdateDTO;

import java.util.Set;


public class ReservationSaveDTO {
    private String status;
    private String startDatetime;
    private String endDatetime;
    private UserUpdateDTO user;
    private Set<VehicleUpdateDTO> vehicles;

    public ReservationSaveDTO() {}

    public ReservationSaveDTO(String status, String startDatetime, String endDatetime, UserUpdateDTO user, Set<VehicleUpdateDTO> vehicles) {
        this.status = status;
        this.startDatetime = startDatetime;
        this.endDatetime = endDatetime;
        this.user = user;
        this.vehicles = vehicles;
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

    public UserUpdateDTO getUser() {
        return user;
    }

    public void setUser(UserUpdateDTO user) {
        this.user = user;
    }

    public Set<VehicleUpdateDTO> getVehicles() {
        return vehicles;
    }

    public void setVehicles(Set<VehicleUpdateDTO> vehicles) {
        this.vehicles = vehicles;
    }
}
