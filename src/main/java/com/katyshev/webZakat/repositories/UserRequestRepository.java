package com.katyshev.webZakat.repositories;

import com.katyshev.webZakat.models.UserRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRequestRepository extends JpaRepository<UserRequest, Integer> {

}
