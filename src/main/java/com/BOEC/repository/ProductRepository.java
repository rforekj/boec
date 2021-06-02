package com.BOEC.repository;

import com.BOEC.model.processing.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface ProductRepository extends CrudRepository<Product, Integer> {

    Page<Product> findAllBy(Pageable pageable);

    Page<Product> findAllByItem_NameIsContaining(String name, Pageable pageable);

    Optional<Product> findByItem_Id(int id);
}
