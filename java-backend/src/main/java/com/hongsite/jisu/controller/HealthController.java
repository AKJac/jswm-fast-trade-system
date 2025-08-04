package com.hongsite.jisu.controller;

import com.hongsite.jisu.common.Result;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

/**
 * 健康检查控制器
 * 
 * @author 鸿思特科技有限公司
 */
@RestController
@RequestMapping("/health")
@Tag(name = "健康检查", description = "系统健康状态检查")
public class HealthController {

    @Value("${spring.application.name:jisu-foreign-trade}")
    private String applicationName;

    @Value("${jisu.company.name:鸿思特科技有限公司}")
    private String companyName;

    @Value("${jisu.company.email:hst_co@163.com}")
    private String companyEmail;

    @Value("${jisu.company.phone:17600198256}")
    private String companyPhone;

    @GetMapping
    @Operation(summary = "健康检查", description = "检查系统运行状态")
    public Result<Map<String, Object>> health() {
        Map<String, Object> data = new HashMap<>();
        data.put("status", "UP");
        data.put("application", applicationName);
        data.put("company", companyName);
        data.put("email", companyEmail);
        data.put("phone", companyPhone);
        data.put("timestamp", LocalDateTime.now());
        data.put("message", "极速外贸管理系统运行正常");
        
        return Result.success("系统运行正常", data);
    }

    @GetMapping("/version")
    @Operation(summary = "版本信息", description = "获取系统版本信息")
    public Result<Map<String, Object>> version() {
        Map<String, Object> data = new HashMap<>();
        data.put("version", "1.0.0");
        data.put("buildTime", "2024-12-01");
        data.put("author", companyName);
        data.put("description", "极速外贸管理系统后端API");
        
        return Result.success("版本信息", data);
    }
}