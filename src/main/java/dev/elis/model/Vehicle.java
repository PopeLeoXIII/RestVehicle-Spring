package dev.elis.model;

import jakarta.persistence.*;

import java.util.List;
import java.util.Objects;

/**
 * Транспортное средство (пока что велосипеды, но вдруг еще что будет, поддерживаем мастшабируемость)
 * Vehicle {
 *     id
 *     city_id - город, в котором находится данная техника
 *     name - название техники
 *     (пока нет) image - изображение техники
 * }
 * Relation:
 * Many To Many: Vehicle <-> Reservation
 * Many To One: Vehicle -> City
 */

@Entity
@Table(name = "vehicles")
public class Vehicle {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "name")
    private String name;

    @ManyToOne(fetch = FetchType.LAZY)
    private City city;

    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(name="reservations_vehicles",
            joinColumns=  @JoinColumn(name="reservation_id", referencedColumnName="id"),
            inverseJoinColumns= @JoinColumn(name="vehicle_id", referencedColumnName="id") )
    private List<Reservation> reservationList;

    public Vehicle(){}

    public Vehicle(Long id, String name, City city, List<Reservation> reservationList) {
        this.id = id;
        this.name = name;
        this.city = city;
        this.reservationList = reservationList;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {this.id = id; }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public City getCity() {
        return city;
    }

    public void setCity(City city) {
        this.city = city;
    }

    public List<Reservation> getReservationList() {
        return reservationList;
    }

    public void setReservationList(List<Reservation> reservationList) {
        this.reservationList = reservationList;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Vehicle vehicle = (Vehicle) o;
        return Objects.equals(id, vehicle.id) && Objects.equals(name, vehicle.name) && Objects.equals(city, vehicle.city);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, city);
    }
}
