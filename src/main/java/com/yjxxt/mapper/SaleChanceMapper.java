package com.yjxxt.mapper;

import com.yjxxt.base.BaseMapper;
import com.yjxxt.bean.SaleChance;

public interface SaleChanceMapper extends BaseMapper<SaleChance,Integer> {
    /*
        由于考虑到多个模块均涉及多条件查询
        这里对于多条件分页查询方法由父接口BaseMapper定义
     */
}