package com.yjxxt.interceptors;

import com.yjxxt.exceptions.NoLoginException;
import com.yjxxt.service.UserService;
import com.yjxxt.utils.LoginUserUtil;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 非法访问拦截
 */
public class NoLoginInterceptor extends HandlerInterceptorAdapter {

    @Resource
    private UserService userService;



    public boolean preHandle(HttpServletRequest request, HttpServletResponse response
                             ,Object handle)throws Exception{
        //获取cookie中的用户ID
        Integer userId = LoginUserUtil.releaseUserIdFromCookie(request);
        //判断用户ID是否不为空，且数据库中存在对应的用户记录
        if(null == userId || null==userService.selectByPrimaryKey(userId)){
            //抛出未登录异常
            throw new NoLoginException();
        }
        return true;
    }
}
