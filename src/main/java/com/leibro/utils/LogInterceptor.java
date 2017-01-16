package com.leibro.utils;

import org.apache.log4j.Logger;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.xml.ws.handler.LogicalHandler;
import java.util.Date;

/**
 * Created by leibro on 2017/1/14.
 */
public class LogInterceptor implements HandlerInterceptor {
    Logger logger = Logger.getLogger(LogInterceptor.class);
    @Override
    public boolean preHandle(javax.servlet.http.HttpServletRequest httpServletRequest, javax.servlet.http.HttpServletResponse httpServletResponse, Object o) throws Exception {
        logger.info(httpServletRequest.getRemoteAddr() + " " + httpServletRequest.getRequestURI());
        return true;
    }

    @Override
    public void postHandle(javax.servlet.http.HttpServletRequest httpServletRequest, javax.servlet.http.HttpServletResponse httpServletResponse, Object o, ModelAndView modelAndView) throws Exception {

    }

    @Override
    public void afterCompletion(javax.servlet.http.HttpServletRequest httpServletRequest, javax.servlet.http.HttpServletResponse httpServletResponse, Object o, Exception e) throws Exception {

    }
}
