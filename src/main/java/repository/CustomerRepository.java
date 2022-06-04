package repository;

import entity.Customer;

import javax.persistence.EntityManager;
import javax.persistence.Persistence;
import java.util.List;

public class CustomerRepository implements Repository<Customer> {
    private EntityManager entityManager;

    public CustomerRepository() {
        entityManager = Persistence.createEntityManagerFactory("test").createEntityManager();
    }

    @Override
    public List<Customer> getAll() {
        return entityManager.createQuery("SELECT c FROM Customer c").getResultList();
    }

    @Override
    public Customer get(int id) {
        entityManager.close();
        entityManager = Persistence.createEntityManagerFactory("test").createEntityManager();
        //Customer customer = entityManager.find(Customer.class, id);
        //entityManager.refresh(customer);
        return entityManager.find(Customer.class, id);
    }

    @Override
    public void save(Customer customer) {
        entityManager.getTransaction().begin();
        entityManager.persist(customer);
        entityManager.getTransaction().commit();
    }

    @Override
    public void update(Customer customer) {
        entityManager.getTransaction().begin();
        entityManager.merge(customer);
        entityManager.getTransaction().commit();
    }

    @Override
    public void delete(Customer customer) {
        entityManager.getTransaction().begin();
        entityManager.remove(customer);
        entityManager.getTransaction().commit();
    }
}
