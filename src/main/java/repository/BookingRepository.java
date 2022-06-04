package repository;

import entity.Booking;

import javax.persistence.EntityManager;
import javax.persistence.Persistence;
import java.util.List;

public class BookingRepository implements Repository<Booking> {
    private EntityManager entityManager;

    public BookingRepository() {
        entityManager = Persistence.createEntityManagerFactory("test").createEntityManager();
    }

    @Override
    public List<Booking> getAll() {
        return entityManager.createQuery("SELECT b FROM Booking b").getResultList();
    }

    @Override
    public Booking get(int id) {
        return entityManager.find(Booking.class, id);
    }

    @Override
    public void save(Booking booking) {
        entityManager.getTransaction().begin();
        entityManager.persist(booking);
        entityManager.getTransaction().commit();
    }

    @Override
    public void update(Booking booking) {
        entityManager.getTransaction().begin();
        entityManager.merge(booking);
        entityManager.getTransaction().commit();
    }

    @Override
    public void delete(Booking booking) {
        entityManager.getTransaction().begin();
        entityManager.remove(booking);
        entityManager.getTransaction().commit();
    }
}
