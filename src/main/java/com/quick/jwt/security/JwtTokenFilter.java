package com.quick.jwt.security;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.GenericFilterBean;

import com.alibaba.fastjson.JSON;
import com.quick.jwt.exception.CustomException;
import com.quick.jwt.exception.ResponseInfo;
import com.quick.jwt.exception.ResultStatusEnum;


public class JwtTokenFilter extends GenericFilterBean {

	private JwtTokenProvider jwtTokenProvider;

	public JwtTokenFilter(JwtTokenProvider jwtTokenProvider) {
		this.jwtTokenProvider = jwtTokenProvider;
	}

	@Override
	public void doFilter(ServletRequest req, ServletResponse res, FilterChain filterChain)
			throws IOException, ServletException {
		String token = jwtTokenProvider.resolveToken((HttpServletRequest) req);
		try {
			if (token != null && jwtTokenProvider.validateToken(token)) {
				Authentication auth = jwtTokenProvider.getAuthentication(token);
				SecurityContextHolder.getContext().setAuthentication(auth);
			}
		} catch (CustomException ex) {
			HttpServletResponse response = (HttpServletResponse) res;
			responseOutWithJson(response);
			return;
		}
		filterChain.doFilter(req, res);
	}

	/**
	 * 返回登录失效信息
	 * @param response
	 */
	protected void responseOutWithJson(HttpServletResponse response) {
		// 将实体对象转换为JSON Object转换
		ResponseInfo<Object> result = new ResponseInfo<>();
		result.setData(null);
		result.setCode(ResultStatusEnum.http_status_forbidden.getErrorCode());
		result.setMsg(ResultStatusEnum.http_status_forbidden.getErrorMsg());
		result.setCurrentDate(System.currentTimeMillis());
		response.setCharacterEncoding("UTF-8");
		response.setContentType("application/json; charset=utf-8");
		PrintWriter out = null;
		try {
			out = response.getWriter();
			out.append(JSON.toJSONString(result));
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (out != null) {
				out.close();
			}
		}
	}
}