package com.jisu.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.License;
import org.springframework.context.annotation.Configuration;

@Configuration
@OpenAPIDefinition(
        info = @Info(
                title = "极速外贸管理系统 API 文档",
                version = "1.0.0",
                description = "后端接口说明，可通过 Swagger UI 在线调试。",
                contact = @Contact(name = "鸿思特科技", email = "support@example.com"),
                license = @License(name = "MIT")
        )
)
public class OpenApiConfig {
}