package repository;

import java.util.List;

public interface Repository<E>{
    List<E> getAll();
    E get(int id);
    void save(E entity);
    void update(E entity);
    void delete(E entity);
}
