package com.katyshev.webZakat.services;

import com.katyshev.webZakat.models.UserRequest;
import com.katyshev.webZakat.repositories.UserRequestRepository;
import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
@Log
public class UserRequestService {
    private final UserRequestRepository userRequestRepository;

    @Autowired
    public UserRequestService(UserRequestRepository userRequestRepository) {
        this.userRequestRepository = userRequestRepository;
    }

    public UserRequest get(int orderGroup) {
        return userRequestRepository.findById(orderGroup).orElse(new UserRequest());
    }


    @Transactional
    public void save(UserRequest userRequest) {
        userRequestRepository.save(userRequest);
    }

    public List<UserRequest> findAll() {
        return userRequestRepository.findAll();
    }


    @Transactional
    public void saveOrUpdate(UserRequest userRequest) {
        Optional<UserRequest> contained = userRequestRepository.findById(userRequest.getOrderGroupId());

        if (contained.isEmpty()) {
            save(userRequest);
        } else {
            update(userRequest);
        }
    }

    @Transactional
    public void update(UserRequest userRequest) {
        save(userRequest);
    }
}
