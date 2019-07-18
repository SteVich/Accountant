package com.example.biblio.payload;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;

@Getter
@Setter
public class ApiResponse {

    @Column(name = "success", nullable = false)
    private Boolean success;

    @Column(name = "message", nullable = false)
    private String message;

    public ApiResponse(Boolean success, String message) {
        this.success = success;
        this.message = message;
    }

}
