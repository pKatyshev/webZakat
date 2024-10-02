package com.katyshev.webZakat.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.Objects;

@Getter
@Setter
@Entity
@Table(name="user_request")
public class UserRequest {

    @Id
    @Column(name="order_group_id")
    private int orderGroupId;
    @Column(name="user_request")
    private String userRequest;

    public UserRequest() {
    }

    public UserRequest(int orderGroupId, String userRequest) {
        this.orderGroupId = orderGroupId;
        this.userRequest = userRequest;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserRequest that = (UserRequest) o;
        return orderGroupId == that.orderGroupId && Objects.equals(userRequest, that.userRequest);
    }

    @Override
    public int hashCode() {
        return Objects.hash(orderGroupId, userRequest);
    }
}
