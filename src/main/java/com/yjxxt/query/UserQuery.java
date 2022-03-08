package com.yjxxt.query;

import com.yjxxt.base.BaseQuery;

public class UserQuery extends BaseQuery {
    //用户名
    private String UserName;
    //邮箱
    private String email;
    //电话
    private String phone;

    public String getUserName() {
        return UserName;
    }

    public void setUserName(String userName) {
        UserName = userName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
}
