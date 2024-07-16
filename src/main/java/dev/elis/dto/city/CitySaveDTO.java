package dev.elis.dto.city;


import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

public class CitySaveDTO {

    @NotNull
    @NotBlank
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
