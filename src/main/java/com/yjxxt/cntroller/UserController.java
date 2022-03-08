package com.yjxxt.cntroller;

import com.yjxxt.annotaions.RequirePermission;
import com.yjxxt.base.BaseController;
import com.yjxxt.base.ResultInfo;
import com.yjxxt.bean.User;
import com.yjxxt.model.UserModel;
import com.yjxxt.query.SaleChanceQuery;
import com.yjxxt.query.UserQuery;
import com.yjxxt.service.SaleChanceService;
import com.yjxxt.service.UserService;
import com.yjxxt.utils.LoginUserUtil;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

@Controller
public class UserController extends BaseController {

    @Resource
    private UserService userService;
    @Resource
    private SaleChanceService saleChanceService;

    @PostMapping("login")
    @ResponseBody
    public ResultInfo userLogin(String userName,String userPwd){

        ResultInfo resultInfo = new ResultInfo();

        // 调用Service层的登录方法，得到返回的用户对象
        UserModel userModel = userService.userLogin(userName,userPwd);
        // 将返回的UserModel对象设置到 ResultInfo 对象中
        resultInfo.setResult(userModel);

        return resultInfo;


    }


    /**
     * 用户密码修改
     * @param request
     * @param oldPassword
     * @param newPassword
     * @param confirmPassword
     * @return
     */
    @RequestMapping("updatePassword")
    @ResponseBody
    public ResultInfo updateUserPassword(HttpServletRequest request,String oldPassword,
                                         String newPassword,String confirmPassword){
        ResultInfo resultInfo = new ResultInfo();

        //获取userId
        Integer userId = LoginUserUtil.releaseUserIdFromCookie(request);
        //调用Service层的密码修改方法
        userService.updateUserPassword(userId,oldPassword,newPassword,confirmPassword);

        return resultInfo;
    }

    @RequestMapping("user/toPasswordPage")
    public String toPasswordPage(){
        return "user/Password";
    }


    /**
     * 查询所有的销售人员
     */
    @RequestMapping("queryAllSales")
    @ResponseBody
    public List<Map<String,Object>> queryAllSales(){
        return userService.queryAllSales();
    }

    /**
     * 多条件查询用户数据
     * @param userQuery
     * @return
     */
    @RequestMapping("user/list")
    @ResponseBody
    public Map<String,Object> queryUserByParams(UserQuery userQuery){
        return userService.queryUserByParams(userQuery);
    }

    /**
     * 进入用户页面
     * @return
     */
    @RequestMapping("/user/index")
    public String index(){
        return "user/user";
    }

    /**
     * 添加用户
     * @param user
     * @return
     */
    @RequestMapping("user/save")
    @ResponseBody
    public ResultInfo saveUser(User user) {
        userService.saveUser(user);
        return success("用户添加成功！");
    }

    /**
     * 更新用户
     * @param user
     * @return
     */
    @RequestMapping("user/update")
    @ResponseBody
    public ResultInfo updateUser(User user) {
        userService.updateUser(user);
        return success("用户更新成功！");
    }

    /**
     * 进入用户添加或更新页面
     * @param id
     * @param model
     * @return
     */
    @RequestMapping("user/addOrUpdateUserPage")
    public String addUserPage(Integer id, Model model){
        if(null != id){
            model.addAttribute("user",userService.selectByPrimaryKey(id));
        }
        return "user/add_update";
    }

    /**
     * 删除用户
     * @param ids
     * @return
     */
    @RequestMapping("/user/delete")
    @ResponseBody
    public ResultInfo deleteUser(Integer[] ids){
        userService.deleteBatch(ids);
        return success("用户记录删除成功");
    }

    @RequestMapping("list")
    @ResponseBody
    @RequirePermission(code = "101001")
    public Map<String,Object> querySaleChancesByParams(Integer flag,
                                                       HttpServletRequest request,
                                                       SaleChanceQuery saleChanceQuery){
        if(null != flag && flag==1){
            //查询分配给当前登录用户 营销记录
            saleChanceQuery.setAssignMan(LoginUserUtil.releaseUserIdFromCookie(request));
        }
        return saleChanceService.queryByParamsForTable(saleChanceQuery);
    }
}
