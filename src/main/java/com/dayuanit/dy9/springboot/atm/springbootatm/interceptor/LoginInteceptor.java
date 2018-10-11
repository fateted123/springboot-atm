package com.dayuanit.dy9.springboot.atm.springbootatm.interceptor;

import com.alibaba.fastjson.JSON;
import com.dayuanit.dy9.springboot.atm.springbootatm.dto.ResponseDTO;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.OutputStream;

@Component
public class LoginInteceptor extends HandlerInterceptorAdapter {

    private ThreadLocal<Long> requestTime= new ThreadLocal<>();

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        long startTime= System.currentTimeMillis();
        requestTime.set(startTime);

        HttpSession session = request.getSession(false);
        boolean isAJax = StringUtils.isNotBlank(request.getHeader("X-Requested-With"));


        if (null == session) {
            if (isAJax) {
                sendRedirectMessage(response);
            } else {
                response.sendRedirect("/toLogin");
            }

            return false;
        }

        Object obj = session.getAttribute("loginUser");
        if (null == obj) {
            if (isAJax) {
                sendRedirectMessage(response);
            } else {
                response.sendRedirect("/toLogin");
            }
            return false;
        }

        return true;
    }


    private void sendRedirectMessage(HttpServletResponse response) {
        try  {
            OutputStream os = response.getOutputStream();
            ResponseDTO dto = ResponseDTO.toLogin();
            os.write(JSON.toJSONString(dto).getBytes("utf-8"));
            os.flush();
        } catch (Exception e) {

        }
    }
    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        long endTime= System.currentTimeMillis();
        long startTime = requestTime.get();
        long timeout = endTime - startTime;
        System.out.println("本次请求耗费时间=" + timeout);
    }
}
