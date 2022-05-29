package entity;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "customers")
public class Customer extends Person {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "customer_id")
    private int id;

    @Column(name = "customer_email")
    private String email;


    //    @OneToMany(cascade = CascadeType.ALL)
//    @JoinColumn(name = "customer_id")
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "customer")
    private List<Booking> bookings;

    public Customer() {
    }

    public Customer(String name, String surname, String email) {
        super(name, surname);
        this.email = email;
    }

    public int getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public List<Booking> getBookings() {
        return bookings;
    }

    public void setBookings(List<Booking> bookings) {
        this.bookings = bookings;
    }

    @Override
    public String toString() {
        return "Customer{" +
                "id=" + id +
                ", email='" + email + '\'' +
                ", bookings=" + bookings +
                '}';
    }

    public String[] toArrayString() {
        return new String[]{Integer.toString(id), getName(),
                getSurname(), getEmail()};
    }
}
