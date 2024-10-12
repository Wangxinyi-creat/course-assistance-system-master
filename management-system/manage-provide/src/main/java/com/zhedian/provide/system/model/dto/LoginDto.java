package com.zhedian.provide.system.model.dto;

import lombok.Data;


@Data
public class LoginDto {

    private String userName;

    private String password;

    private String code;

    private String uuid;
}
