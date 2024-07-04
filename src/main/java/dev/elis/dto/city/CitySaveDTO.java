package dev.elis.dto.city;

import javax.validation.constraints.Size;

public class CitySaveDTO {

    @Size(min = 3, max = 20, message = "Введите Ваше имя!")
    private String name;

    public CitySaveDTO() {
    }

    public CitySaveDTO(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}
