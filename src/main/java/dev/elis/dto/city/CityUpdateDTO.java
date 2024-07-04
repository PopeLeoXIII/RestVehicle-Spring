package dev.elis.dto.city;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

public class CityUpdateDTO {

    @NotEmpty
    private Long id;

    @Size(min = 3, max = 20, message = "Введите Ваше имя!")
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
