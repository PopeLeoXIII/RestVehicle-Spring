package dev.elis;

import dev.elis.dto.city.CityUpdateDTO;
import dev.elis.dto.reservation.ReservationUpdateDTO;
import dev.elis.dto.user.UserUpdateDTO;
import dev.elis.dto.vehicle.VehicleUpdateDTO;
import dev.elis.model.*;

import java.util.List;
import java.util.Set;

public class TestData {
    private final User user;
    private final Vehicle vehicle;
    private final City city;
    private final Reservation reservation;

    public TestData() {
        city = new City(1L, "City", List.of());
        user = new User(2L, "User name", "surname", List.of());
        vehicle = new Vehicle(3L, "Vehicle", city, Set.of());
        reservation = new Reservation(1L, Status.ACTIVE, "2016-06-22 19:10", "2016-06-23 19:10", user, Set.of());
        city.setVehicles(List.of(vehicle));
        user.setReservations(List.of(reservation));
        vehicle.setReservations(Set.of(reservation));
        reservation.setVehicles(Set.of(vehicle));
    }

    public User getUser() {
        return user;
    }

    public Vehicle getVehicle() {
        return vehicle;
    }

    public City getCity() {
        return city;
    }

    public Reservation getReservation() {
        return reservation;
    }

    public UserUpdateDTO getUserDto() {
        return new UserUpdateDTO(user.getId(), user.getName(), user.getSurname());
    }

    public VehicleUpdateDTO getVehicleDto() {
        return new VehicleUpdateDTO(vehicle.getId(), vehicle.getName(), getCityDto());
    }

    public CityUpdateDTO getCityDto() {
        return new CityUpdateDTO(city.getId(), city.getName());
    }

    public ReservationUpdateDTO getReservationDto() {
        return new ReservationUpdateDTO(
                reservation.getId(),
                reservation.getStatus().toString(),
                reservation.getStartDatetime().toString(),
                reservation.getEndDatetime().toString(),
                getUserDto(),
                Set.of(getVehicleDto()));
    }
}
