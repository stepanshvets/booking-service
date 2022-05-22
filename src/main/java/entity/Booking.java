package entity;

import javax.persistence.*;

@Entity
@Table(name = "bookings")
public class Booking {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @Column(name = "booking_start")
    private String start;

    @Column(name = "booking_end")
    private String end;

    @ManyToOne
    @JoinColumn(name = "apartment_id")
    private Apartment apartment;

    @ManyToOne
    @JoinColumn(name = "customer_id")
    private Customer customer;

    public Booking() {
    }

    public Booking(String start, String end) {
        this.start = start;
        this.end = end;
    }

    public int getId() {
        return id;
    }

    public String getStart() {
        return start;
    }

    public void setStart(String start) {
        this.start = start;
    }

    public String getEnd() {
        return end;
    }

    public void setEnd(String end) {
        this.end = end;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public Apartment getApartment() {
        return apartment;
    }

    public void setApartment(Apartment apartment) {
        this.apartment = apartment;
    }

    @Override
    public String toString() {
        return "Booking{" +
                "id=" + id +
                ", start='" + start + '\'' +
                ", end='" + end + '\'' +
                ", apartmentId=" + apartment.getId() +
                ", customerId=" + customer.getId() +
                '}';
    }

    public String[] toArrayString() {
        return new String[] {Integer.toString(id), start, end,
                Integer.toString(apartment.getId()), Integer.toString(customer.getId())};
    }
}
