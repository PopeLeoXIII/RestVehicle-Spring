package dev.elis.dto.vehicle;

import dev.elis.dto.city.CityUpdateDTO;

import javax.validation.constraints.NotEmpty;

public class VehicleUpdateDTO {

    @NotEmpty
    private Long id;
    private String name;
    private CityUpdateDTO city;

    public VehicleUpdateDTO() {}

    public VehicleUpdateDTO(Long id, String name, CityUpdateDTO city) {
        this.id = id;
        this.name = name;
        this.city = city;
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
}
