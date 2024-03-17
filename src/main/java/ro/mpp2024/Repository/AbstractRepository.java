package ro.mpp2024.Repository;

import ro.mpp2024.Domain.Entity;

import java.util.List;
import java.util.Optional;

public interface AbstractRepository<ID,E extends Entity<ID>>{
    public Optional<E> add(E e);
    public Optional<E> update(E e);
    public Optional<E> deleteById(ID id);
    public Optional<E> findById(ID id);
    public List<E> findAll();
}
