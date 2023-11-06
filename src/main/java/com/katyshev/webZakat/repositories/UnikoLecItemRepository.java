package com.katyshev.webZakat.repositories;

import com.katyshev.webZakat.models.UnikoLecItem;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UnikoLecItemRepository extends JpaRepository<UnikoLecItem, Integer> {

    @Modifying
    @Transactional
    @Query(value = "TRUNCATE TABLE uniko_lec_item", nativeQuery = true)
    void truncateTable();

    @Modifying
    @Transactional
    @Query(value = "ALTER SEQUENCE uniko_lec_item_id_seq RESTART WITH 1", nativeQuery = true)
    void restartSequence();

    @Query(value = "SELECT MAX(id) FROM uniko_lec_item", nativeQuery = true)
    Optional<Integer> getMaxId();
}
