package com.yjxxt.mapper;

import com.yjxxt.base.BaseMapper;
import com.yjxxt.bean.Role;

import java.util.List;
import java.util.Map;

public interface RoleMapper extends BaseMapper<Role,Integer> {
    // 查询角色列表
    //@MapKey("")
    
    public List<Map<String,Object>> queryAllRoles();

    Role queryRoleByRoleName(String roleName);
}