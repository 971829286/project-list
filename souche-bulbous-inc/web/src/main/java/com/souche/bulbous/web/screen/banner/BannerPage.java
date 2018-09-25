package com.souche.bulbous.web.screen.banner;

import com.souche.optimus.core.annotation.View;
import com.souche.optimus.core.controller.Context;
import com.souche.optimus.core.controller.Navigator;
import com.souche.optimus.core.sad.AbstractReactScreen;

import javax.servlet.http.HttpServletRequest;

/**
 * @author xujinniu@souche.com
 * @Description: banner配置转发类
 * @date 2018/9/3
 */
@View
public class BannerPage extends AbstractReactScreen {

    public void execute(Context context, Navigator nav, HttpServletRequest request) {
        super.buildContext(context, request);
        nav.forwardTo("cms");
    }
}
