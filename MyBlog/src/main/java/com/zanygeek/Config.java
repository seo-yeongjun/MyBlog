package com.zanygeek;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.zanygeek.interceptor.OwnerInterceptor;

@Configuration
public class Config implements WebMvcConfigurer {

	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		registry.addInterceptor(authInterceptor()).order(1).addPathPatterns("/*/content/**","/*/categoty/**").excludePathPatterns("/css/**",
				"/", "/members/add", "/login", "/logout", "/*.ico", "/error","/api/item/*","/error/**","/error-page/**");
	}

	@Bean
    public OwnerInterceptor authInterceptor(){
    	return new OwnerInterceptor();
    }
}
