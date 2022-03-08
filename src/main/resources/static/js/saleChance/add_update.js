layui.use(['form', 'layer'], function () {
    var form = layui.form,
        layer = parent.layer === undefined ? layui.layer : top.layer,
        $ = layui.jquery;

    /**
     * 监听submit事件
     *      实现营销机会的添加与更新
     */
    form.on("submit(addOrUpdateSaleChance)",function (data){
        //提交数据时的加载层（https://layer.layui.com/）
        var index = layer.msg("数据提交中，请稍等...",{
            icon:16, //图标
            time:false,//不关闭
            shade:0.8 //设置遮罩的透明度
        });
        //请求地址
        var url = ctx + "/sale_chance/save";

        // 判断隐藏域中的ID是否为空，如果不为空则为修改操作
        if ($("input[name='id']").val()) {
            url = ctx + "/sale_chance/update";
        }
        //发送ajax
        $.post(url,data.field,function (result){
            //操作成功
            if(result.code == 200){
                //提示成功
                layer.msg("操作成功！");
                //关闭加载层
                layer.close(index);
                //关闭弹出层
                layer.closeAll("iframe");
                //刷新父页面，重新渲染表格数据
                parent.location.reload();
            }else{
                layer.msg(result.msg);
            }
        },"json");
        return false;
    });
    /**
     * 关闭弹出层
     */
    $("#closeBtn").click(function (){
        //先得到当前的iframe层的索引
        var index = parent.layer.getFrameIndex(window.name);
        //在执行关闭
        parent.layer.close(index);
    })

    /**
     * 加载下来框
     */
    /*发送ajax填充分配人*/

    $.ajax({
        type:"post",
        url:ctx+"/user/sales",
        dataType:"json",
        success:function (data){
            //指派人ID
            var assignMan=$("#man").val();
            //循环遍历
            for(var x in data){
                //追加option
                if(assignMan==data[x].id){
                    $("#assignMan").append("<option selected value='"+data[x].id+"'>"+data[x].uname+"</option>");
                }else{
                    $("#assignMan").append("<option  value='"+data[x].id+"'>"+data[x].uname+"</option>");
                }
            }
            //重新渲染下拉框
            layui.form.render("select");
        }
    });
});