package com.wicc.personalassistant.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ResponseDto {
    private boolean status;
    private String message;
    private Object object;

    public ResponseDto(boolean status, String message){
        this.status = status;
        this.message = message;
    }
    public ResponseDto(String message){
        this.message = message;
    }
}
