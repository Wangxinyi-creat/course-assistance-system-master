package com.zhedian.provide.system.service.Impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.ObjectUtils;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zhedian.provide.system.entity.SysDept;
import com.zhedian.provide.system.mapper.SysDeptMapper;
import com.zhedian.provide.system.service.SysDeptService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 部门表 服务实现类
 */
@Service
public class SysDeptServiceImpl extends ServiceImpl<SysDeptMapper, SysDept> implements SysDeptService {


    @Override
    public List<SysDept> getDeptList(String deptName, Integer status) {
        LambdaQueryWrapper<SysDept> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.like(ObjectUtils.isNotNull(deptName),SysDept::getDeptName,deptName)
                .eq(ObjectUtils.isNotNull(status),SysDept::getStatus,status)
                .orderByAsc(SysDept::getSortNum);
        List<SysDept> depts = list(queryWrapper);
        //构造树形数据返回
        return buildTree(depts);
    }

    /**
     * 构造树形数据
     *
     * @param depts 部门列表
     * @return
     */
    private List<SysDept> buildTree(List<SysDept> depts) {
        //获取顶级部门  父节点是0的数据
        List<SysDept> sysDepts = depts.stream().filter(SysDept -> SysDept.getParentId() == 0).collect(Collectors.toList());
        if (sysDepts.size() == 0) return depts;

        //获取每个顶级部门的子数据
        for (SysDept dept : sysDepts) {
            //获取子部门列表
            List<SysDept> children = getChildren(dept.getId(), depts);
            if (children.size() > 0) dept.setChildren(children);
        }
        return sysDepts;
    }

    /**
     * 递归查找子节点
     *
     * @param parentId  父节点ID
     * @param deptsList 部门列表
     * @return
     */
    private List<SysDept> getChildren(Integer parentId, List<SysDept> deptsList) {
        //所属父节点ID的子部门
        List<SysDept> childrenList = deptsList.stream().filter(SysDept -> SysDept.getParentId().equals(parentId)).collect(Collectors.toList());

        //递归查找子部门下的子节点
        if (ObjectUtils.isNotEmpty(childrenList)) {
            for (SysDept dept : childrenList) {
                List<SysDept> children = getChildren(dept.getId(), deptsList);
                if (CollectionUtils.isNotEmpty(children)) dept.setChildren(children);
            }
        }
        //返回子部门
        return childrenList;
    }
}




