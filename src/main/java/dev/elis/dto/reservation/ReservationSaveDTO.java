package dev.elis.dto.reservation;

import dev.elis.dto.user.UserUpdateDTO;
import dev.elis.dto.vehicle.VehicleUpdateDTO;

import java.util.List;


public class ReservationSaveDTO {
    private String status;
    private String startDatetime;
    private String endDatetime;
    private UserUpdateDTO user;
    private List<VehicleUpdateDTO> vehicleList;

    public ReservationSaveDTO() {}

    public ReservationSaveDTO(String status, String startDatetime, String endDatetime, List<VehicleUpdateDTO> vehicles, UserUpdateDTO user) {
        this.status = status;
        this.startDatetime = startDatetime;
        this.endDatetime = endDatetime;
        this.vehicleList = vehicles;
        this.user = user;
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

    public List<VehicleUpdateDTO> getVehicleList() {
        return vehicleList;
    }

    public void setVehicleList(List<VehicleUpdateDTO> vehicleList) {
        this.vehicleList = vehicleList;
    }
}
