package cn.ourwill.huiyizhan.controller;


import cn.ourwill.huiyizhan.entity.Demo;
import cn.ourwill.huiyizhan.interceptor.Access;
import io.swagger.annotations.*;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author Jixuan.Lin @ourwill.com.cn
 * @Time 2017/7/24 0024 10:17
 * @Version1.0
 */
@RestController
@RequestMapping("/demo")
@Api(value = "测试Swagger2",description="简单的API")
public class DemoController {

    @ApiOperation(value = "创建用户", notes = "根据User对象创建用户")
    @ApiImplicitParams({
            @ApiImplicitParam(dataType = "java.lang.Long", name = "id", value = "id", required = true, paramType = "path"),
            @ApiImplicitParam(dataType = "Demo", name = "demo", value = "用户信息", required = true)
    })
    @ApiResponses({
            @ApiResponse(code = 500, message = "接口异常"),
    })
    @RequestMapping(value = "/user/{id}", method = RequestMethod.POST)
    public Demo insert(@PathVariable Long id, @RequestBody Demo demo) {

        System.out.println("id:" + id + ", demo:" + demo);
        demo.setId(id);

        return demo;
    }

    @ApiOperation(value = "获取指定id用户详细信息", notes = "根据user的id来获取用户详细信息")
    @ApiImplicitParam(name = "id", value = "用户id", dataType = "String", paramType = "path")
    @RequestMapping(value = "/user/{id}", method = RequestMethod.GET)
    @Access
    public Demo getUser(@PathVariable Long id) {

        Demo demo = new Demo();
        demo.setId(id);
        demo.setPassword("abc");
        demo.setUsername("12345");
        return demo;
    }

    @ApiOperation(value = "获取所有用户详细信息", notes = "获取用户列表信息")
    @RequestMapping(value = "/users", method = RequestMethod.GET)
    public List<Demo> getUserList() {

        List<Demo> list = new ArrayList<>();
        Demo demo = new Demo();
        demo.setId(15L);
        demo.setPassword("ricky");
        demo.setUsername("root");

        list.add(demo);

        return list;
    }

//    @ApiIgnore
    @ApiOperation(value = "删除指定id用户", notes = "根据id来删除用户信息")
    @ApiImplicitParam(name = "id", value = "用户id", dataType = "java.lang.Long", paramType = "path")
    @RequestMapping(value = "/user/{id}", method = RequestMethod.DELETE)
    public String delete(@PathVariable Long id) {
        System.out.println("delete user:" + id);
        return "OK";
    }
}
