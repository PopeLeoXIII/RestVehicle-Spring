package dev.elis.model;

import jakarta.persistence.*;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.Set;

/**
 * Бронирование
 * Reservation {
 *     id
 *     vehicle_ids (M-to-M) - список забронированной техники
 *     user_id (M-to-O) - пользователь, кто забронировал
 *     startDatetime - с какого времени
 *     endDatetime - до какого времени
 *     status [ACTIVE, CANCELED] - статус бронирования (активно / отменено)
 * }
 * Relation:
 * Many To Many: Reservation <-> Vehicle
 * One To Many: Reservation -> User
 */

@Entity
@Table(name = "reservations")
public class Reservation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private Status status;

    @Column(name = "start_datetime")
    private Timestamp startDatetime;

    @Column(name = "end_datetime")
    private Timestamp endDatetime;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="user_id", nullable=false)
    private User user;

    @ManyToMany(mappedBy = "reservations", fetch = FetchType.LAZY,
            cascade = {CascadeType.MERGE, CascadeType.REFRESH})
    private Set<Vehicle> vehicles;

    public void addVehicle(Vehicle vehicle) {
        this.vehicles.add(vehicle);
        vehicle.getReservations().add(this);
    }

    public void removeVehicle(Vehicle vehicle) {
        this.vehicles.remove(vehicle);
        vehicle.getReservations().remove(this);
    }

    @PreRemove
    public void preRemove() {
        for(Vehicle vehicle: vehicles) {
            vehicle.getReservations().remove(this);
        }
    }


    public Reservation() {}

    public Reservation(Long id, Status status, Timestamp startDatetime, Timestamp endDatetime, Set<Vehicle> vehicles, User user) {
        this.id = id;
        this.status = status;
        this.startDatetime = startDatetime;
        this.endDatetime = endDatetime;
        this.user = user;
        this.vehicles = vehicles;
    }

    public Reservation(Long id, Status status, String startDatetime, String endDatetime, User user, Set<Vehicle> vehicles) {
        this.id = id;
        this.status = status;
        this.startDatetime = stringToTimestamp(startDatetime);
        this.endDatetime = stringToTimestamp(endDatetime);
        this.user = user;
        this.vehicles = vehicles;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public Timestamp getStartDatetime() {
        return startDatetime;
    }

    public void setStartDatetime(Timestamp startDatetime) {
        this.startDatetime = startDatetime;
    }

    public Timestamp getEndDatetime() {
        return endDatetime;
    }

    public void setEndDatetime(Timestamp endDatetime) {
        this.endDatetime = endDatetime;
    }

    public Set<Vehicle> getVehicles() {
        return vehicles;
    }

    public void setVehicles(Set<Vehicle> vehicleList) {
        this.vehicles = vehicleList;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public static Timestamp stringToTimestamp(String stringDate) {
        try {
            DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm");
            Date parsedDate = dateFormat.parse(stringDate);
            return new Timestamp(parsedDate.getTime());
        } catch(Exception e) {
            return null;
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Reservation that = (Reservation) o;
        return Objects.equals(id, that.id) && status == that.status && Objects.equals(startDatetime, that.startDatetime) && Objects.equals(endDatetime, that.endDatetime) && Objects.equals(user.getId(), that.user.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, status, startDatetime, endDatetime, user);
    }
}
