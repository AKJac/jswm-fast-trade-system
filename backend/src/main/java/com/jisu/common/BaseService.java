package com.jisu.common;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public abstract class BaseService<T extends BaseEntity, R extends BaseRepository<T>> {

    @Autowired
    protected R repo;

    public Page<T> list(Pageable pageable) {
        return repo.findAll(pageable);
    }

    public T get(String id) {
        return repo.findById(id).orElseThrow();
    }

    public T save(T entity) {
        return repo.save(entity);
    }

    public void delete(String id) {
        repo.deleteById(id);
    }
}