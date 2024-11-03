package com.katyshev.webZakat.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ServerResponse {
    private String status;

    public ServerResponse(String status) {
        this.status = status;
    }
}
