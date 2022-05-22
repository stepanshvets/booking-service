import entity.Apartment;
import org.junit.*;
import repository.ApartmentRepository;

public class TestRepository {
    final private ApartmentRepository apartmentDAO;

    public TestRepository() {
        this.apartmentDAO = new ApartmentRepository();
    }

    @Test
    public void testAdd(){
        Apartment apartment = new Apartment("New Apartment address", 5, 5);
        apartmentDAO.save(apartment);
        Assert.assertNotNull(apartmentDAO.get(apartment.getId()));
    }

    @Test
    public void testEdit(){
        Apartment apartment = new Apartment("New Apartment address", 5, 5);
        apartmentDAO.save(apartment);
        apartment.setAddress("Edited Apartment address");
        apartment.setSeats(5);
        apartmentDAO.update(apartment);
        Assert.assertEquals(apartmentDAO.get(apartment.getId()).getAddress(), "Edited Apartment address");
        Assert.assertEquals(apartmentDAO.get(apartment.getId()).getSeats(), "Edited Apartment seats");
    }

    @Test
    public void testDelete(){
        Apartment apartment = new Apartment("New Apartment address", 5, 5);
        apartmentDAO.save(apartment);
        apartmentDAO.delete(apartment);
        Assert.assertNull(apartmentDAO.get(apartment.getId()));
    }
}
