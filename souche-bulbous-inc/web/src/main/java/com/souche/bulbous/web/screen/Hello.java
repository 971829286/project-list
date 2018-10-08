package com.souche.bulbous.web.screen;

import com.souche.optimus.core.annotation.View;
import com.souche.optimus.core.controller.Context;
import com.souche.optimus.core.controller.Navigator;
import com.souche.optimus.core.sad.AbstractReactScreen;

import javax.servlet.http.HttpServletRequest;

/**
 * @author XadomGreen
 * @since 2018-08-17
 */
@View
public class Hello extends AbstractReactScreen {
    public void execute(Context context, Navigator nav, HttpServletRequest request) {
        super.buildContext(context, request);
        nav.forwardTo("default");
    }
}
