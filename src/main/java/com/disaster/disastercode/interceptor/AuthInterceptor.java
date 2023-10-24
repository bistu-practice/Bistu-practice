package com.disaster.disastercode.interceptor;


import com.disaster.disastercode.common.ErrorCode;
import com.disaster.disastercode.exception.BusinessException;
import com.disaster.disastercode.model.domain.User;
import com.disaster.disastercode.utils.JWTUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class AuthInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)  {
        String token = request.getHeader(JWTUtils.header);
        if (StringUtils.isBlank(token))
            throw new BusinessException(ErrorCode.NOT_LOGIN);

        //获得权限等级，开始验证
        User user = JWTUtils.validateToken(token);
        //检验通过
        request.setAttribute(JWTUtils.currentUserKey, user);
        return true;

    }


}
