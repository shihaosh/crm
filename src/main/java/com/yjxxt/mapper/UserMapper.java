package com.yjxxt.mapper;

import com.yjxxt.base.BaseMapper;
import com.yjxxt.bean.User;

import java.util.List;
import java.util.Map;

public interface UserMapper extends BaseMapper<User,Integer> {

    //新增方法
    public User queryUserByUserName(String userName);
    // 查询所有的销售人员
    public List<Map<String, Object>> queryAllSales();
}