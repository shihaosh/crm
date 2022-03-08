package com.yjxxt.cntroller;

import com.yjxxt.base.BaseController;
import com.yjxxt.dto.TreeDto;
import com.yjxxt.service.ModuleService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.util.List;

@Controller
@RequestMapping("module")
public class ModuleController extends BaseController {

    @Resource
    private ModuleService moduleService;



//    @RequestMapping("queryAllModules")
//    @ResponseBody
//    public List<TreeDto> queryAllModules(){
//        return moduleService.queryAllModules();
//    }
    @RequestMapping("queryAllModules")
    @ResponseBody
    public List<TreeDto> queryAllModules(Integer roleId){
        return moduleService.queryAllModules02(roleId);
    }

}
