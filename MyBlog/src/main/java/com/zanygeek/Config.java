package com.zanygeek;

import com.zanygeek.interceptor.BlogInterceptor;
import com.zanygeek.interceptor.BlogVisitInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class Config implements WebMvcConfigurer {
	@Autowired
	BlogInterceptor blogInterceptor;
	@Autowired
	BlogVisitInterceptor blogVisitInterceptor;
	

	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		registry.addInterceptor(blogInterceptor).order(1).addPathPatterns("/**").excludePathPatterns("/css/**",
				"/js/**", "/", "/member/**", "/login", "/logout", "/*.ico", "/error", "/api/item/*", "/error/**",
				"/error-page/**","/images/**","/image","/test/test","/thumbnail","/thumbnail/**");
		registry.addInterceptor(blogVisitInterceptor).order(2).addPathPatterns("/**").excludePathPatterns("/error",
				"/", "/member/**", "/*.ico", "/*/content/edit/**", "/*/content/add", "/css/**", "/js/**", "/login",
				"/logout","/images/**","/image","/test/test","/thumbnail","/thumbnail/**");
	}
}
