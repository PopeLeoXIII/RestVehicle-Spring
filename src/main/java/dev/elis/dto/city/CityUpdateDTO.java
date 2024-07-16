package dev.elis.dto.city;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

public class CityUpdateDTO {

    @NotNull
    private Long id;

    @NotNull
    @NotBlank
    private String name;

    public CityUpdateDTO() {
    }

    public CityUpdateDTO(Long id, String name) {
        this.id = id;
        this.name = name;
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
}
