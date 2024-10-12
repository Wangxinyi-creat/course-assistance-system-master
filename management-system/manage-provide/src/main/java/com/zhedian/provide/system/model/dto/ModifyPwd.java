package com.zhedian.provide.system.model.dto;

import lombok.Data;


@Data
public class ModifyPwd {

    private String oldPassword;

    private String newPassword1;

    private String newPassword2;
}
