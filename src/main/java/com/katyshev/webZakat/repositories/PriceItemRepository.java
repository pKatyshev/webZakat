package com.katyshev.webZakat.repositories;

import com.katyshev.webZakat.models.PriceItem;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.Optional;

@Repository
public interface PriceItemRepository extends JpaRepository<PriceItem, Integer>  {

    @Modifying
    @Transactional
    @Query(value = "TRUNCATE TABLE price_item CASCADE", nativeQuery = true)
    void truncateTable();

    @Modifying
    @Transactional
    @Query(value = "ALTER SEQUENCE price_item_id_seq RESTART WITH 1", nativeQuery = true)
    void restartSequence();

    @Modifying
    @Transactional
    @Query(value = "UPDATE price_item SET in_order = 0 WHERE in_order != 0", nativeQuery = true)
    void clearInOrderColumn();

    @Query(value ="SELECT SUM(price * in_order) FROM price_item WHERE in_order > 0", nativeQuery = true)
    Optional<BigDecimal> getTotalAmount();
}
