package com.souche.bulbous.web.screen;

import com.souche.optimus.core.annotation.View;
import com.souche.optimus.core.controller.Context;
import com.souche.optimus.core.controller.Navigator;
import com.souche.optimus.core.sad.AbstractReactScreen;

import javax.servlet.http.HttpServletRequest;

/**
 * @Description：服务工具配置页
 *
 * @remark: Created by wujingtao in 2018/9/10
 *
 * 谨以此页面献给各位老师
 **/
@View
public class ServiceToolScreen extends AbstractReactScreen{

    public void execute(Context context, Navigator nav, HttpServletRequest request) {
        super.buildContext(context, request);
        nav.forwardTo("cms");
    }
}
