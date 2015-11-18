package com.newput.service;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.context.support.SpringBeanAutowiringSupport;

/**
 * To manage session request of user.
 * 
 * @author Newput
 *
 */
@Service
public class SessionFilter implements Filter {

	@Autowired
	private LoginService loginService;

	public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain)
			throws IOException, ServletException {

		HttpServletRequest request = (HttpServletRequest) req;
		HttpServletResponse response = (HttpServletResponse) res;

		String path = request.getRequestURI().substring(23);

		if (path.equals("register") || path.equals("login") || path.equals("verify") || path.equals("forgotPwd") 
				|| path.equals("pwdVerify") || path.equals("resend")|| path.equals("excelExport")) {
					
			chain.doFilter(req, res);
		} else {
			response.setHeader("Access-Control-Allow-Origin", "*");
			response.setHeader("Access-Control-Allow-Methods", "GET, PUT, DELETE, POST");
        	response.setHeader("Access-Control-Allow-Headers", "token, empId");
        	
//			String token = request.getHeader("token");
//			String emp_id = request.getHeader("empId");
			String token = request.getParameter("token");
			String emp_id = request.getParameter("empId");			
			if (token == null || token.equals("") || emp_id == null || emp_id.equals("")) {
				response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
			} else {				
				if (loginService.loginSessionFilter(token, Integer.parseInt(emp_id))) {
					response.setHeader("response status",
							"" + loginService.loginSessionFilter(token, Integer.parseInt(emp_id)));					
					chain.doFilter(req, res);
				} else {
					response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
				}
			}
		}
	}

	@Override
	public void init(FilterConfig config) throws ServletException {
		SpringBeanAutowiringSupport.processInjectionBasedOnServletContext(this, config.getServletContext());
	}

	@Override
	public void destroy() {
		// TODO Auto-generated method stub
	}
}
