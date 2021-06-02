package com.BOEC.repository;

import com.BOEC.model.item.Item;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

public interface ItemRepository extends CrudRepository<Item, Integer> {

    Page<Item> findAllByOrderByCreatedDateDesc(Pageable pageable);

    Page<Item> findByNameIsContaining(String name, Pageable pageable);

    @Modifying
    @Transactional
    @Query(value = "update item set is_publish = 0 where id = :id", nativeQuery = true)
    void updateIsPublic(@Param("id") int id);
}
