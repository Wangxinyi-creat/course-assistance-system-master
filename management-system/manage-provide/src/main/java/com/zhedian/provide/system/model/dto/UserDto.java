package com.zhedian.provide.system.model.dto;

import com.zhedian.provide.system.entity.SysUser;
import lombok.Data;

import java.util.List;


@Data
public class UserDto {

    private SysUser user;

    private List<Integer> roleSign;

}
