package com.vinplay.api.server;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.vinplay.usercore.utils.GameThirdPartyInit;

public class CorsFilter implements Filter {
	public void init(FilterConfig filterConfig) throws ServletException {
	}

	public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain)
			throws IOException, ServletException {
		HttpServletResponse httpResponse = (HttpServletResponse) servletResponse;
		httpResponse.setHeader("Access-Control-Allow-Origin", "*");
		httpResponse.setContentType("application/json;charset=UTF-8");
		httpResponse.setHeader("Access-Control-Allow-Credentials", "true");
		httpResponse.setHeader("Access-Control-Allow-Methods", "POST, GET, HEAD, OPTIONS");
		httpResponse.setHeader("Access-Control-Allow-Headers",
				"Origin, Accept, X-Requested-With, Content-Type, Access-Control-Request-Method, Access-Control-Request-Headers");
		filterChain.doFilter(servletRequest, servletResponse);
//		String[] allowDomain = { 
//				"https://roy88.vip", 
//				"https://play.roy88.vip",
//				"https://roy88.vip", 
//				"https://roy88.vip", 
//				"https://roy88.vip", 
//				"https://roy88.vip"
//		};
//		Set<String> allowedOrigins = new HashSet<String>(Arrays.asList(allowDomain));
//		String originHeader = ((HttpServletRequest) servletRequest).getHeader("Origin");
//		if ("pro".equals(GameThirdPartyInit.enviroment)) {
//			if (allowedOrigins.contains(originHeader)) {
//				httpResponse.setHeader("Access-Control-Allow-Origin", originHeader);
//				httpResponse.setContentType("application/json;charset=UTF-8");
//				httpResponse.setHeader("Access-Control-Allow-Credentials", "true");
//				httpResponse.setHeader("Access-Control-Allow-Methods", "POST, GET, HEAD, OPTIONS");
//				httpResponse.setHeader("Access-Control-Allow-Headers",
//						"Origin, Accept, X-Requested-With, Content-Type, Access-Control-Request-Method, Access-Control-Request-Headers");
//				filterChain.doFilter(servletRequest, servletResponse);
//			}
//		} else {// "http://localhost:7456",
////			"http://localhost:8081",
//			httpResponse.setHeader("Access-Control-Allow-Origin", "*");
//			httpResponse.setContentType("application/json;charset=UTF-8");
//			httpResponse.setHeader("Access-Control-Allow-Credentials", "true");
//			httpResponse.setHeader("Access-Control-Allow-Methods", "POST, GET, HEAD, OPTIONS");
//			httpResponse.setHeader("Access-Control-Allow-Headers",
//					"Origin, Accept, X-Requested-With, Content-Type, Access-Control-Request-Method, Access-Control-Request-Headers");
//			filterChain.doFilter(servletRequest, servletResponse);
//		}

	}

	public void destroy() {
	}
}
