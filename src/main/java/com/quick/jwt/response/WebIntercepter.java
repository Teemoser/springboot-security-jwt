package com.quick.jwt.response;


import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import com.quick.jwt.model.Role;
import com.quick.jwt.security.JwtTokenProvider;


/**
 * @author xiaojin_wu
 * @email wuxiaojin258@126.com
 * @date 2018年3月10日
 * @description Intercepter
 */
@Component
public class WebIntercepter implements HandlerInterceptor {

	@Value("${security.jwt.token.universal}")
	private String universalToken;
	
	@Autowired
	private JwtTokenProvider jwtTokenProvider;
	
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		String origin = request.getHeader("Origin");
		response.setHeader("Access-Control-Allow-Origin", origin);
		response.setHeader("Access-Control-Allow-Methods", "*");
		response.setHeader("Access-Control-Allow-Headers","Origin,Content-Type,Accept,token,X-Requested-With");
		response.setHeader("Access-Control-Allow-Credentials", "true");
		//
		List<Role> roles = new ArrayList<Role>();
		roles.add(Role.ROLE_CLIENT);
		String token = jwtTokenProvider.createToken(universalToken, roles);
		response.setHeader("token", token);
		return true;
	}

	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
			ModelAndView modelAndView) throws Exception {
	}

	@Override
	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)
			throws Exception {
	}
}
