layui.use(['form','jquery','jquery_cookie'], function () {
    var form = layui.form,
        layer = layui.layer,
        $ = layui.jquery,
        $ = layui.jquery_cookie($);

    /**
     * 用户登录  表单提交
     */
    form.on("submit(login)",function (data){
        //获取表单元素的值（用户+密码）
        var fieldData = data.field;

        //判断参数是否为空
        if(fieldData.username=="undefined" || fieldData.username.trim() == ""){
            layer.msg("用户名不能为空！");
            return false;
        }
        if(fieldData.password=="undefined" || fieldData.password.trim()==""){
            layer.msg("用户名密码不能为空");
            return false;
        }

        //发送ajax请求，请求用户登录
        $.ajax({
            type:"post",
            url:ctx+"/login",
            data:{
                userName:fieldData.username,
                userPwd:fieldData.password
            },
            success:function (data){
                //判断是否登陆成功
                if(data.code == 200){
                    layer.msg("登陆成功！",{icon:6},function (){
                        //将用户信息存在cookie中
                        var result = data.result;
                        $.cookie("userIdStr", result.userIdStr);
                        $.cookie("userName",result.userName);
                        $.cookie("userPwd",result.userPwd);

                        //如果用户选择"记住我"，则设置cookie的有效期为7天
                        if($("input[type='checkbox']").is(":checked")){
                            $.cookie("userIdStr",result.userIdStr,{expires:7});
                            $.cookie("userName",result.userName,{expires:7});
                            $.cookie("userPwd",result.userPwd,{expires:7});
                        }

                        //登陆成功后，跳转到首页
                        window.location.href = ctx + "/main";
                    });
                }else{
                    //提示信息
                    layer.msg(data.msg,{icon:5});
                }
            }
        });
        //阻止表单跳转
        return false;
    })

});