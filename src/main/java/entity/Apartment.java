package entity;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "apartments")
public class Apartment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "apartment_id")
    private int id;

    @Column(name = "apartment_address")
    private String address;

    @Column(name = "apartment_seats")
    private int seats;

    @Column(name = "apartment_price")
    private int price;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "apartment")
    private List<Booking> bookings;

    public Apartment() {
    }

    public Apartment(String address, int seats, int price) {
        this.address = address;
        this.seats = seats;
        this.price = price;
    }

    public int getId() {
        return id;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public int getSeats() {
        return seats;
    }

    public void setSeats(int seats) {
        this.seats = seats;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public List<Booking> getBookings() {
        return bookings;
    }

    public void setBookings(List<Booking> bookings) {
        this.bookings = bookings;
    }

    @Override
    public String toString() {
        return "Apartment{" +
                "id=" + id +
                ", address='" + address + '\'' +
                ", seats='" + seats + '\'' +
                ", booking=" + bookings +
                '}';
    }

    public String[] toArrayString() {
        return new String[] {Integer.toString(id), address, Integer.toString(seats), Integer.toString(price)};
    }
}
