package dev.elis.model;

import jakarta.persistence.*;

import java.util.List;

/**
 * Город, в котором существует техника для бронирования
 * City {
 *     id
 *     name - название города
 *     (пока нет) timezone - таймзона города
 *  }
 * Relation:
 * One To Many: City -> Vehicle
 */

@Entity
@Table(name = "cities")
public class City {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "name")
    private String name;

    @OneToMany(mappedBy = "city",orphanRemoval = true, fetch = FetchType.LAZY)
    private List<Vehicle> vehicles;

    public City() {
    }

    public City(Long id, String name, List<Vehicle> vehicles) {
        this.id = id;
        this.name = name;
        this.vehicles = vehicles;
    }

    public void addVehicle(Vehicle vehicle) {
        vehicles.add(vehicle);
        vehicle.setCity(this);
    }
    public void removeVehicle(Vehicle vehicle) {
        vehicles.remove(vehicle);
        vehicle.setCity(null);
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

    public List<Vehicle> getVehicles() {
        return vehicles;
    }

    public void setVehicles(List<Vehicle> vehicleList) {
        this.vehicles = vehicleList;
    }
}
