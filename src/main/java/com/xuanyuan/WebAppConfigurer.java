package com.xuanyuan;

import java.util.ArrayList;
import java.util.List;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import com.xuanyuan.auth.interceptors.AuthInterceptor;

@Configuration
public class WebAppConfigurer extends WebMvcConfigurerAdapter {
	
	/**
	 * 配置拦截器
	 */
	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		// TODO Auto-generated method stub
		// 多个拦截器组成一个拦截器链
		// addPathPatterns 用于添加拦截规则
		// excludePathPatterns 用户排除拦截
		List<String> includeUrls = new ArrayList<String>();
		List<String> exceptUrls = new ArrayList<String>();
		includeUrls.add("*.mvc");
		includeUrls.add("*.jsp");
		includeUrls.add("*.html");
		exceptUrls.add("/admin.html");
		exceptUrls.add("/randCodeImage");
		exceptUrls.add("/config/cfgSysconfig/loadCache.mvc*");
		exceptUrls.add("/xyplugins/*");
		exceptUrls.add("/swagger/*");
		exceptUrls.add("/plugin/*");
		exceptUrls.add("/scripts/*");
		exceptUrls.add("/auth/*");
		exceptUrls.add("/sound/*");
		exceptUrls.add("/widgets/*");
		InterceptorRegistration auth = registry.addInterceptor(new AuthInterceptor(includeUrls,exceptUrls));
		auth.addPathPatterns("/**");
		super.addInterceptors(registry);
	}

	/**
	 * 添加自定义的静态资源映射 这里使用代码的方式自定义目录映射，并不影响Spring Boot的默认映射，可以同时使用。
	 */
	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {
		registry.addResourceHandler("/app/**").addResourceLocations("/WEB-INF/jsp/app/");
		registry.addResourceHandler("/api/**").addResourceLocations("/WEB-INF/jsp/api/");
		// registry.addResourceHandler("/**").addResourceLocations("/");
		super.addResourceHandlers(registry);
	}
	
}
