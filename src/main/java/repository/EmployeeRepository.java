package repository;

import entity.Employee;

import javax.persistence.EntityManager;
import javax.persistence.Persistence;
import java.util.List;

public class EmployeeRepository implements Repository<Employee>{
    private EntityManager entityManager;

    public EmployeeRepository() {
        entityManager = Persistence.createEntityManagerFactory("test").createEntityManager();
    }

    @Override
    public List<Employee> getAll() {
        return entityManager.createQuery("SELECT e FROM Employee e").getResultList();
    }

    @Override
    public Employee get(int id) {
        return entityManager.find(Employee.class, id);
    }

    @Override
    public void save(Employee employee) {
        entityManager.getTransaction().begin();
        entityManager.persist(employee);
        entityManager.getTransaction().commit();
    }

    @Override
    public void update(Employee employee) {
        entityManager.getTransaction().begin();
        entityManager.merge(employee);
        entityManager.getTransaction().commit();
    }

    @Override
    public void delete(Employee employee) {
        entityManager.getTransaction().begin();
        entityManager.remove(employee);
        entityManager.getTransaction().commit();
    }
}
