package repository;

import entity.Apartment;

import javax.persistence.EntityManager;
import javax.persistence.Persistence;
import java.util.List;

public class ApartmentRepository implements Repository<Apartment> {
    private EntityManager entityManager;

    public ApartmentRepository(){
        entityManager = Persistence.createEntityManagerFactory("test").createEntityManager();
    }

    @Override
    public List<Apartment> getAll() {
        entityManager = Persistence.createEntityManagerFactory("test").createEntityManager();
        return entityManager.createQuery("SELECT a FROM Apartment a").getResultList();
    }

    @Override
    public Apartment get(int id) {
        return entityManager.find(Apartment.class, id);
    }

    @Override
    public void save(Apartment apartment) {
        entityManager.getTransaction().begin();
        entityManager.persist(apartment);
        entityManager.getTransaction().commit();
    }

    @Override
    public void update(Apartment apartment) {
        entityManager.getTransaction().begin();
        entityManager.merge(apartment);
        entityManager.getTransaction().commit();
    }

    @Override
    public void delete(Apartment apartment) {
        entityManager.getTransaction().begin();
        entityManager.remove(apartment);
        entityManager.getTransaction().commit();
    }

    public List<Apartment> getByPrice(int minPrice, int maxPrice) {
        return entityManager.createQuery("SELECT a FROM Apartment a WHERE a.price >="
                + minPrice + "AND a.price<=" + maxPrice).getResultList();
    }

    public List<Apartment> getByMinPrice(int minPrice) {
        return entityManager.createQuery("SELECT a FROM Apartment a WHERE a.price>=" + minPrice).getResultList();
    }

    public List<Apartment> getByMaxPrice(int maxPrice) {
        return entityManager.createQuery("SELECT a FROM Apartment a WHERE a.price<=" + maxPrice).getResultList();
    }
}
