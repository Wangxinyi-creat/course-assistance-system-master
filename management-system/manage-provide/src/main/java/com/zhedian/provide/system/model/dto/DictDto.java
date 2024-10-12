package com.zhedian.provide.system.model.dto;

import com.zhedian.provide.system.entity.SysDictData;
import com.zhedian.provide.system.entity.SysDictType;
import lombok.Data;

import java.util.List;

/**
 * 字典DTO
 */
@Data
public class DictDto {

    private SysDictType dictType;

    private List<SysDictData> dictDataList;

}
