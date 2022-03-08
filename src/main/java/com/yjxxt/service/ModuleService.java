package com.yjxxt.service;

import com.yjxxt.base.BaseService;

import com.yjxxt.bean.Module;
import com.yjxxt.dto.TreeDto;
import com.yjxxt.mapper.ModuleMapper;
import com.yjxxt.mapper.PermissionMapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class ModuleService extends BaseService<Module,Integer> {


    @Resource
    private ModuleMapper moduleMapper;
    @Resource
    private PermissionMapper permissionMapper;

    public List<TreeDto> queryAllModules(){
        return moduleMapper.queryAllModules();
    }

    public List<TreeDto> queryAllModules02(Integer roleId) {
        List<TreeDto> treeDtos=moduleMapper.queryAllModules();
        // 根据角色id 查询角色拥有的菜单id List<Integer>
        List<Integer>
                roleHasMids=permissionMapper.queryRoleHasAllModuleIdsByRoleId(roleId);
        if(null !=roleHasMids && roleHasMids.size()>0){
            treeDtos.forEach(treeDto -> {
                if(roleHasMids.contains(treeDto.getId())){
                    // 说明当前角色 分配了该菜单
                    treeDto.setChecked(true);
                }
            });
        }
        return  treeDtos;
    }
}
