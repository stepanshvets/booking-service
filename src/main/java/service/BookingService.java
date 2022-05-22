package service;

import entity.Apartment;
import entity.Booking;
import entity.Customer;
import exception.ValidationException;
import exception.NoSuchObject;
import repository.BookingRepository;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.List;

public class BookingService {
    final private ApartmentService apartmentService;
    final private CustomerService customerService;
    final private BookingRepository bookingRepository;

    public BookingService(ApartmentService apartmentService, CustomerService customerService,
                          BookingRepository bookingRepository) {
        this.apartmentService = apartmentService;
        this.customerService = customerService;
        this.bookingRepository = bookingRepository;
    }

    public List<Booking> getAllBookings(){
        return bookingRepository.getAll();
    }

    public Booking getBooking(int id) {
        return bookingRepository.get(id);
    }

    public void saveBooking(Booking booking) {
        bookingRepository.save(booking);
    }

    public void updateBooking(Booking booking) {
        bookingRepository.update(booking);
    }

    public void deleteBooking(Booking booking) {
        bookingRepository.delete(booking);
    }

    public String[][] toArrayArrayString(List<Booking> bookings){
        String[][] arrayString = new String[bookings.size()][];
        int i = 0;
        for (Booking booking: bookings)
            arrayString[i++] = booking.toArrayString();
        return arrayString;
    }

    public void setBooking(Booking booking, String[] arrayString) throws NoSuchObject,
            ValidationException, DateTimeParseException {
        isNotEmpty(arrayString);
        LocalDate dateStart;
        LocalDate dateEnd;
        try {
            dateStart = LocalDate.parse(arrayString[0]);
            dateEnd = LocalDate.parse(arrayString[1]);
            if (dateEnd.isBefore(dateStart) || dateStart.isBefore(LocalDate.now()))
                throw new ValidationException("Wrong date!\nShould be yyyy-mm-dd");
        }
        catch (DateTimeParseException exception){
            throw new ValidationException("Date should be yyyy-mm-dd!");
        }
        catch (ValidationException exception){
            throw new ValidationException("Wrong date!");
        }

        booking.setStart(arrayString[0]);
        booking.setEnd(arrayString[1]);

        Customer customer;
        Apartment apartment;

        try {
            customer = customerService.getCustomer(Integer.parseInt(arrayString[3]));
            apartment = apartmentService.getApartment(Integer.parseInt(arrayString[2]));
        }
        catch (IllegalArgumentException exception) {
            throw new ValidationException("Wrong Filling!");
        }

        if (customer == null)
            throw new NoSuchObject("There is not such customer!");
        booking.setCustomer(customer);

        if (apartment == null)
            throw new NoSuchObject("There is not such apartment!");
        else {
            for (Booking b : apartment.getBookings()){
                LocalDate otherDateStart = LocalDate.parse(b.getStart());
                LocalDate otherDateEnd = LocalDate.parse(b.getEnd());
                if (!(dateStart.isBefore(otherDateStart) && dateEnd.isBefore(otherDateStart) ||
                        dateStart.isAfter(otherDateStart) && dateStart.isAfter(otherDateEnd)))
                    throw new ValidationException("There is already booking at this date!");
            }
        }
        booking.setApartment(apartment);
    }

    void isNotEmpty(String[] arrayString) throws ValidationException {
        for (String string : arrayString)
            if (string.equals(""))
                throw new ValidationException("Some fields are empty!");
    }
}
