package dev.elis.dto.city;

import dev.elis.dto.vehicle.VehicleResponseDTO;

import java.util.List;

public class CityResponseDTO {

    private Long id;
    private String name;
    private List<VehicleResponseDTO> vehicles;

    public CityResponseDTO() {
    }

    public CityResponseDTO(Long id, String name, List<VehicleResponseDTO> vehicles) {
        this.id = id;
        this.name = name;
        this.vehicles = vehicles;
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

    public List<VehicleResponseDTO> getVehicles() {
        return vehicles;
    }

    public void setVehicles(List<VehicleResponseDTO> vehicles) {
        this.vehicles = vehicles;
    }
}
