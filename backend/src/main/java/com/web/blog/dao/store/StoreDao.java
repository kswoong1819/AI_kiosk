package com.web.blog.dao.store;

import com.web.blog.model.store.Store;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface StoreDao extends JpaRepository<Store, Integer> {
    Store findBySid(int sid);

    @Query(nativeQuery = true, value = "select storename from store where sid=:sid")
    String test(int sid);
}