package com.example.biblio.payload;

import lombok.*;


@Getter
@Setter

public class ApiResponse {

    private Boolean success;
    private String message;

    public ApiResponse(Boolean success, String message) {
        this.success = success;
        this.message = message;
    }

}
