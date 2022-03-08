package com.yjxxt.interceptors;

import com.yjxxt.exceptions.NoLoginException;
import com.yjxxt.service.UserService;
import com.yjxxt.utils.LoginUserUtil;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * �Ƿ���������
 */
public class NoLoginInterceptor extends HandlerInterceptorAdapter {

    @Resource
    private UserService userService;



    public boolean preHandle(HttpServletRequest request, HttpServletResponse response
                             ,Object handle)throws Exception{
        //��ȡcookie�е��û�ID
        Integer userId = LoginUserUtil.releaseUserIdFromCookie(request);
        //�ж��û�ID�Ƿ�Ϊ�գ������ݿ��д��ڶ�Ӧ���û���¼
        if(null == userId || null==userService.selectByPrimaryKey(userId)){
            //�׳�δ��¼�쳣
            throw new NoLoginException();
        }
        return true;
    }
}
