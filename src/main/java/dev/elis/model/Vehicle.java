package dev.elis.model;

import jakarta.persistence.*;

import java.util.Objects;
import java.util.Set;

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

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="city_id", nullable=false)
    private City city;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name="reservations_vehicles",
            joinColumns=  @JoinColumn(name="vehicle_id", referencedColumnName="id"),
            inverseJoinColumns= @JoinColumn(name="reservation_id", referencedColumnName="id") )
    private Set<Reservation> reservations;

    public void addReservation(Reservation reservation) {
        this.reservations.add(reservation);
        reservation.getVehicles().add(this);
    }

    public void removeReservation(Reservation reservation) {
        this.reservations.remove(reservation);
        reservation.getVehicles().remove(this);
    }

    @PreRemove
    public void preRemove() {
        for(Reservation reservation: reservations) {
            reservation.getVehicles().remove(this);
        }
    }

    public Vehicle(){}

    public Vehicle(Long id, String name, City city, Set<Reservation> reservations) {
        this.id = id;
        this.name = name;
        this.city = city;
        this.reservations = reservations;
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

    public Set<Reservation> getReservations() {
        return reservations;
    }

    public void setReservations(Set<Reservation> reservationList) {
        this.reservations = reservationList;
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
