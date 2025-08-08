package com.hongsite.jisu.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

/**
 * 跨域配置类
 * 
 * @author 鸿思特科技有限公司
 */
@Configuration
public class CorsConfig {

    @Bean
    public CorsFilter corsFilter() {
        CorsConfiguration config = new CorsConfiguration();
        
        // 允许所有来源
        config.addAllowedOriginPattern("*");
        
        // 允许所有头信息
        config.addAllowedHeader("*");
        
        // 允许所有方法
        config.addAllowedMethod("*");
        
        // 允许携带认证信息
        config.setAllowCredentials(true);
        
        // 预检请求的有效期，单位为秒
        config.setMaxAge(3600L);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);
        
        return new CorsFilter(source);
    }
}