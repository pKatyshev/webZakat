package com.katyshev.webZakat.repositories;

import com.katyshev.webZakat.models.PriceItem;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface PriceItemRepository extends JpaRepository<PriceItem, Integer>  {

    @Modifying
    @Transactional
    @Query(value = "TRUNCATE TABLE price_item", nativeQuery = true)
    void truncateTable();

    @Modifying
    @Transactional
    @Query(value = "ALTER SEQUENCE price_item_id_seq RESTART WITH 1", nativeQuery = true)
    void restartSequence();
}
