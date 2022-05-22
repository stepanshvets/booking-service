package service;

import entity.Apartment;
import entity.Booking;
import exception.ValidationException;
import repository.ApartmentRepository;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.LinkedList;
import java.util.List;

public class ApartmentService {
    final private ApartmentRepository apartmentRepository;

    public ApartmentService(ApartmentRepository apartmentRepository) {
        this.apartmentRepository = apartmentRepository;
    }

    public List<Apartment> getAllApartments(){
        return apartmentRepository.getAll();
    }

    public Apartment getApartment(int id) {
        return apartmentRepository.get(id);
    }

    public void saveApartment(Apartment apartment) {
        apartmentRepository.save(apartment);
    }

    public void updateApartment(Apartment apartment) {
        apartmentRepository.update(apartment);
    }

    public void deleteApartment(Apartment apartment) {
        apartmentRepository.delete(apartment);
    }

    public List<Apartment> getApartmentsByParameters(String[] arrayString) throws DateTimeParseException, ValidationException {
        isNotEmpty(new String[]{arrayString[0], arrayString[1]});
        LocalDate dateStart;
        LocalDate dateEnd;
        try {
            dateStart = LocalDate.parse(arrayString[0]);
            dateEnd = LocalDate.parse(arrayString[1]);
            if (dateEnd.isBefore(dateStart))
                throw new ValidationException("Wrong date!");
        }
        catch (DateTimeParseException exception){
            throw new ValidationException("Date should be yyyy-mm-dd!");
        }
        catch (ValidationException exception){
            throw new ValidationException("Wrong date!");
        }

        List<Apartment> availableApartments = new LinkedList<>();
        List<Apartment> apartments;
        if (arrayString[2].equals("") || arrayString[3].equals(""))
            apartments = apartmentRepository.getAll();
        else{
            try {
                int minPrice = Integer.parseInt(arrayString[2]), maxPrice = Integer.parseInt(arrayString[3]);
                apartments = apartmentRepository.getByPrice(minPrice, maxPrice);
            }
            catch (IllegalArgumentException exception) {
                throw new ValidationException("Wrong Filling!");
            }
        }
        for (Apartment apartment : apartments) {
            boolean isFree = true;
            for (Booking b : apartment.getBookings()) {
                LocalDate otherDateStart = LocalDate.parse(b.getStart());
                LocalDate otherDateEnd = LocalDate.parse(b.getEnd());
                if (!(dateStart.isBefore(otherDateStart) && dateEnd.isBefore(otherDateStart) ||
                        dateStart.isAfter(otherDateStart) && dateStart.isAfter(otherDateEnd)))
                    isFree = false;
            }
            if (isFree)
                availableApartments.add(apartment);
        }

        return availableApartments;
    }

    public String[][] toArrayArrayString(List<Apartment> apartments ){
        String[][] arrayString = new String[apartments.size()][];
        int i = 0;
        for (Apartment apartment: apartments)
            arrayString[i++] = apartment.toArrayString();
        return arrayString;
    }

    public void setApartment(Apartment apartment, String[] arrayString) throws ValidationException {
        isNotEmpty(arrayString);
        apartment.setAddress(arrayString[0]);
        try {
            apartment.setSeats(Integer.parseInt(arrayString[1]));
            apartment.setSeats(Integer.parseInt(arrayString[2]));
        }
        catch (IllegalArgumentException exception) {
            throw new ValidationException("Wrong Filling!");
        }
    }

    void isNotEmpty(String[] arrayString) throws ValidationException {
        for (String string : arrayString)
            if (string.equals(""))
                throw new ValidationException("Some fields are empty!");
    }
}
