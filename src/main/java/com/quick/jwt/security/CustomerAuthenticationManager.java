package com.quick.jwt.security;

import java.util.ArrayList;
import java.util.List;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import com.quick.jwt.model.Role;


/**
 * @author xiaojin_wu
 * @email wuxiaojin258@126.com
 * @date 2018年3月8日
 * @description 
 * 		认证授权类，使用spring注入方式调用
 * 		controller主动发起认证授权操作
 */
@Component
public class CustomerAuthenticationManager implements AuthenticationManager {
	
	static final List<GrantedAuthority> AUTHORITIES = new ArrayList<GrantedAuthority>();
	 
 	/**
 	 * 构建一个角色列表，预留扩充角色
 	 */
 	static {
 		AUTHORITIES.add(Role.ROLE_ADMIN);
 	}
	
	/**
	 * 自定义验证方法
	 */
	public Authentication authenticate(Authentication auth) throws AuthenticationException {
		return new UsernamePasswordAuthenticationToken(auth.getName(), auth.getCredentials(), AUTHORITIES);
	}
	
	/**
	 * 认证授权
	 * @param openid
	 */
	public void auth(String username) {
		Authentication request = new UsernamePasswordAuthenticationToken(username, username);
		// 将token传递给Authentication进行验证
		Authentication result = authenticate(request);
		SecurityContextHolder.getContext().setAuthentication(result);
	}
	
}