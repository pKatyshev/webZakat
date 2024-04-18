package com.katyshev.webZakat.repositories;

import com.katyshev.webZakat.models.ArchiveItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository

public interface ArchiveItemRepository extends JpaRepository<ArchiveItem, Integer> {

    @Query(value = "SELECT MAX(order_number) FROM archive_item", nativeQuery = true)
    int getLastOrderNumber();
}
