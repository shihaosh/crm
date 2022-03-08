package com.yjxxt.cntroller;

import com.yjxxt.base.BaseController;
import com.yjxxt.bean.User;
import com.yjxxt.service.PermissionService;
import com.yjxxt.service.UserService;
import com.yjxxt.utils.LoginUserUtil;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Controller
public class IndexController extends BaseController {
    @Resource
    private UserService userService;
    @Resource
    private PermissionService permissionService;

    @RequestMapping("index")
    public String index(){
        return "index";
    }


    @RequestMapping("welcome")
    public String welcome(){
        return "welcome";
    }


    @RequestMapping("main")
    public String main(HttpServletRequest request){
        //通过工具类，从cookie中获得userID
        Integer userId = LoginUserUtil.releaseUserIdFromCookie(request);
        // 调用对应Service层的方法，通过userId主键查询用户对象
        User user = userService.selectByPrimaryKey(userId);
        //将用户对象设置到request作用域中
        request.setAttribute("user",user);
        request.setAttribute("user",userService.selectByPrimaryKey(userId));
        List<String> permissions =
                permissionService.queryUserHasRolesHasPermissions(userId);
        request.getSession().setAttribute("permissions",permissions);
        return "main";
    }



}
