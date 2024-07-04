package dev.elis.dto.vehicle;

import dev.elis.dto.city.CityUpdateDTO;

public class VehicleSaveDTO {
    private String name;
    private CityUpdateDTO city;

    public VehicleSaveDTO() {}

    public VehicleSaveDTO(String name, CityUpdateDTO city) {
        this.name = name;
        this.city = city;
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
}
