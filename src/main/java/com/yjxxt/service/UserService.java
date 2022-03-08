package com.yjxxt.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.yjxxt.base.BaseService;
import com.yjxxt.bean.User;
import com.yjxxt.mapper.UserMapper;
import com.yjxxt.model.UserModel;
import com.yjxxt.query.UserQuery;
import com.yjxxt.utils.AssertUtil;
import com.yjxxt.utils.Md5Util;
import com.yjxxt.utils.PhoneUtil;
import com.yjxxt.utils.UserIDBase64;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class UserService extends BaseService<User,Integer> {

    @Resource
    private UserMapper userMapper;

    /**
     * 用户登录
     * @param userName
     * @param userPwd
     * @return
     */
    public UserModel userLogin(String userName,String userPwd){
        //验证参数
        checkLoginParms(userName,userPwd);
        //根据用户名，查询用户对象
        User user = userMapper.queryUserByUserName(userName);
        //判断用户是否存在
        AssertUtil.isTrue(null==user,"用户不存在");
        //用户对象不为空
        checkLoginPwd(userPwd,user.getUserPwd());
        //密码正确（用户登录成功）
        return buildUserInfo(user);
    }

    /**
     * 构建返回的用户信息
     * @param user
     * @return
     */
    private UserModel buildUserInfo(User user) {
        UserModel userModel = new UserModel();
        //设置用户信息
        userModel.setUserIdStr(UserIDBase64.encoderUserID(user.getId()));
        userModel.setUserName(user.getUserName());
        userModel.setUserPwd(user.getUserPwd());

        return userModel;
    }

    private void checkLoginPwd(String userPwd,String upwd) {
        // 数据库中的密码是经过加密的，将前台传递的密码先加密，再与数据库中的密码作比较
        userPwd = Md5Util.encode(userPwd);
        //比较密码
        AssertUtil.isTrue(!userPwd.equals(upwd),"用户密码不正确");
    }

    private void checkLoginParms(String userName, String userPwd) {
        //判断用户名
        AssertUtil.isTrue(StringUtils.isBlank(userName),"用户名不能为空");
        AssertUtil.isTrue(StringUtils.isBlank(userPwd),"用密码不能为空");
    }

    /**
     * 用户密码修改
     *         1.参数效验
     *              用户ID：userID  非空  用户对象必须存在
     *              原始密码：oldPassword 非空  与数据库中密文密码保持一致
     *              新密码：newPassword 非空  与原始密码不能相同
     *              确认密码：confirmPassword  非空  与新密码相同
     *         2.设置用户新密码
     *              新密码进行加密处理
     *         3.执行更新操作
     *              收影响的行数小于1，则表示修改失败
     *
     *         注：在对应的更新方法上，添加事务控制
     */


    public void updateUserPassword(Integer userId,String oldPassword,
                                   String newPassword,String confirmPassword){
        //通过userID获取用户对象
        User user = userMapper.selectByPrimaryKey(userId);
        //参数效验
        checkPasswordParams(user,oldPassword,newPassword,confirmPassword);
        //设置用户新密码
        user.setUserPwd(Md5Util.encode(newPassword));
        //执行更新操作
        AssertUtil.isTrue(userMapper.updateByPrimaryKeySelective(user)<1,"用户密码更新失败");
    }

    /**
     * 验证用户密码修改参数
     *          用户ID：userId 非空 用户对象必须存在
     *          原始密码：oldPassword 非空 与数据库中密文密码保持一致
     *          新密码：newPassword 非空 与原始密码不能相同
     *          确认密码：confirmPassword 非空 与新密码保持一致
     * @param user
     * @param oldPassword
     * @param newPassword
     * @param confirmPassword
     */
    private void checkPasswordParams(User user,String oldPassword,
                                     String newPassword,String confirmPassword){
        //user对象  非空验证
        AssertUtil.isTrue(null==user,"用户未登录！");
        //原始密码  非空验证
        AssertUtil.isTrue(StringUtils.isBlank(oldPassword),"请输入原始密码！");
        //原始密码要与数据库中的密文密码保持一致
        AssertUtil.isTrue(!(user.getUserPwd().equals(Md5Util.encode(oldPassword))),"原始密码不正确");
        //新密码  非空验证
        AssertUtil.isTrue(StringUtils.isBlank(newPassword),"请输入新密码");
        //新密码与原始密码不能相同
        AssertUtil.isTrue(oldPassword.equals(newPassword),"新密码不能原始密码相同");
        //确认密码 非空验证
        AssertUtil.isTrue(StringUtils.isBlank(confirmPassword),"请确认密码");
        //新密码与确认密码保持一致
        AssertUtil.isTrue(!(newPassword.equals(confirmPassword)),"新密码与确认密码不一致");

    }

    public List<Map<String, Object>> queryAllSales() {
        return userMapper.queryAllSales();
    }

    public Map<String,Object> queryUserByParams(UserQuery query){
        Map<String,Object> map = new HashMap<>();
        PageHelper.startPage(query.getPage(),query.getLimit());
        PageInfo<User> pageInfo = new PageInfo<>(userMapper.selectByParams(query));
        map.put("code",0);
        map.put("msg","");
        map.put("count",pageInfo.getTotal());
        map.put("data",pageInfo.getList());
        return map;
    }

    /**
     * 添加用户
     * @param user
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public void saveUser(User user) {
        // 1. 参数校验
        checkParams(user.getUserName(), user.getEmail(), user.getPhone());
        // 2. 设置默认参数
        user.setIsValid(1);
        user.setCreateDate(new Date());
        user.setUpdateDate(new Date());
        user.setUserPwd(Md5Util.encode("123456"));
        // 3. 执行添加，判断结果
        AssertUtil.isTrue(userMapper.insertSelective(user) == null, "用户添加失败！");
    }

    /**
     * 参数校验
     * @param userName
     * @param email
     * @param phone
     */
    private void checkParams(String userName, String email, String phone) {
        AssertUtil.isTrue(StringUtils.isBlank(userName), "用户名不能为空！");
        // 验证用户名是否存在
        User temp = userMapper.queryUserByUserName(userName);
        AssertUtil.isTrue(null != temp, "该用户已存在！");
        AssertUtil.isTrue(StringUtils.isBlank(email), "请输入邮箱地址！");
        AssertUtil.isTrue(!PhoneUtil.isMobile(phone), "手机号码格式不正确！");
    }

    /**
     * 更新用户
     * @param user
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public void updateUser(User user) {
        // 1. 参数校验
        // 通过id查询用户对象
        User temp = userMapper.selectByPrimaryKey(user.getId());
        // 判断对象是否存在
        AssertUtil.isTrue(temp == null, "待更新记录不存在！");
        // 验证参数
        checkParam(user.getId(),user.getUserName(),user.getEmail(),user.getPhone());
        // 2. 设置默认参数
        temp.setUpdateDate(new Date());
        // 3. 执行更新，判断结果
        AssertUtil.isTrue(userMapper.updateByPrimaryKeySelective(user) < 1, "用户更新失败！");
    }

    private void checkParam(Integer userId,String userName, String email, String phone) {
        AssertUtil.isTrue(StringUtils.isBlank(userName), "用户名不能为空！");
        // 验证用户名是否存在
        User temp = userMapper.queryUserByUserName(userName);
        AssertUtil.isTrue(null != temp && !(temp.getId().equals(userId)), "该用户已存在！");
        AssertUtil.isTrue(StringUtils.isBlank(email), "请输入邮箱地址！");
        AssertUtil.isTrue(!PhoneUtil.isMobile(phone), "手机号码格式不正确！");
    }
    /**
     * 删除用户
     * @param ids
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public void deleteUserByIds(Integer[] ids) {
        AssertUtil.isTrue(null==ids || ids.length == 0,"请选择待删除的用户记录!");
        AssertUtil.isTrue(deleteBatch(ids) != ids.length,"用户记录删除失败!");
    }
}
