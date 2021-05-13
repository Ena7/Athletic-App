package repo;

import domain.Entity;

import java.util.List;

public interface IRepository<ID, E extends Entity<ID>> {
    ID save(E entity);
    List<E> getAll();
    E getById(ID id);

}

