package com.hongsite.jisu.controller;

import com.hongsite.jisu.common.Result;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * 文件上传控制器
 * 
 * @author 鸿思特科技有限公司
 */
@Slf4j
@RestController
@RequestMapping("/upload")
@Tag(name = "文件上传", description = "文件上传相关操作")
public class FileUploadController {

    @Value("${jisu.upload.path:./uploads/}")
    private String uploadPath;

    @Value("${server.servlet.context-path:/api}")
    private String contextPath;

    @PostMapping("/image")
    @Operation(summary = "上传图片", description = "上传产品图片、头像等图片文件")
    public Result<Map<String, String>> uploadImage(
            @Parameter(description = "图片文件") @RequestParam("file") MultipartFile file) {
        
        if (file.isEmpty()) {
            return Result.error("请选择要上传的图片");
        }
        
        // 检查文件类型
        String contentType = file.getContentType();
        if (contentType == null || !contentType.startsWith("image/")) {
            return Result.error("只能上传图片文件");
        }
        
        // 检查文件大小（5MB）
        if (file.getSize() > 5 * 1024 * 1024) {
            return Result.error("图片文件大小不能超过5MB");
        }
        
        try {
            String fileName = saveFile(file, "images");
            Map<String, String> result = new HashMap<>();
            result.put("fileName", fileName);
            result.put("url", contextPath + "/upload/file/" + fileName);
            result.put("originalName", file.getOriginalFilename());
            result.put("size", String.valueOf(file.getSize()));
            
            return Result.success("图片上传成功", result);
        } catch (Exception e) {
            log.error("图片上传失败", e);
            return Result.error("图片上传失败：" + e.getMessage());
        }
    }

    @PostMapping("/excel")
    @Operation(summary = "上传Excel文件", description = "上传Excel文件用于批量导入")
    public Result<Map<String, String>> uploadExcel(
            @Parameter(description = "Excel文件") @RequestParam("file") MultipartFile file) {
        
        if (file.isEmpty()) {
            return Result.error("请选择要上传的Excel文件");
        }
        
        // 检查文件类型
        String originalFilename = file.getOriginalFilename();
        if (originalFilename == null || 
            (!originalFilename.endsWith(".xlsx") && !originalFilename.endsWith(".xls"))) {
            return Result.error("只能上传Excel文件（.xlsx或.xls）");
        }
        
        // 检查文件大小（10MB）
        if (file.getSize() > 10 * 1024 * 1024) {
            return Result.error("Excel文件大小不能超过10MB");
        }
        
        try {
            String fileName = saveFile(file, "excel");
            Map<String, String> result = new HashMap<>();
            result.put("fileName", fileName);
            result.put("url", contextPath + "/upload/file/" + fileName);
            result.put("originalName", file.getOriginalFilename());
            result.put("size", String.valueOf(file.getSize()));
            
            return Result.success("Excel文件上传成功", result);
        } catch (Exception e) {
            log.error("Excel文件上传失败", e);
            return Result.error("Excel文件上传失败：" + e.getMessage());
        }
    }

    @PostMapping("/document")
    @Operation(summary = "上传文档", description = "上传PDF、Word等文档文件")
    public Result<Map<String, String>> uploadDocument(
            @Parameter(description = "文档文件") @RequestParam("file") MultipartFile file) {
        
        if (file.isEmpty()) {
            return Result.error("请选择要上传的文档");
        }
        
        // 检查文件类型
        String originalFilename = file.getOriginalFilename();
        if (originalFilename == null) {
            return Result.error("文件名不能为空");
        }
        
        String extension = originalFilename.substring(originalFilename.lastIndexOf(".")).toLowerCase();
        if (!extension.matches("\\.(pdf|doc|docx|txt)")) {
            return Result.error("只能上传PDF、Word或文本文档");
        }
        
        // 检查文件大小（20MB）
        if (file.getSize() > 20 * 1024 * 1024) {
            return Result.error("文档文件大小不能超过20MB");
        }
        
        try {
            String fileName = saveFile(file, "documents");
            Map<String, String> result = new HashMap<>();
            result.put("fileName", fileName);
            result.put("url", contextPath + "/upload/file/" + fileName);
            result.put("originalName", file.getOriginalFilename());
            result.put("size", String.valueOf(file.getSize()));
            
            return Result.success("文档上传成功", result);
        } catch (Exception e) {
            log.error("文档上传失败", e);
            return Result.error("文档上传失败：" + e.getMessage());
        }
    }

    @GetMapping("/file/{fileName}")
    @Operation(summary = "获取文件", description = "根据文件名获取上传的文件")
    public void getFile(@PathVariable String fileName) {
        // 这里可以实现文件下载逻辑
        // 由于是简化版本，暂时不实现具体的文件服务逻辑
    }

    /**
     * 保存文件到指定目录
     */
    private String saveFile(MultipartFile file, String subDir) throws IOException {
        // 创建上传目录
        String dateDir = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy/MM/dd"));
        String fullDir = uploadPath + subDir + "/" + dateDir;
        File dir = new File(fullDir);
        if (!dir.exists()) {
            dir.mkdirs();
        }
        
        // 生成唯一文件名
        String originalFilename = file.getOriginalFilename();
        String extension = originalFilename.substring(originalFilename.lastIndexOf("."));
        String fileName = UUID.randomUUID().toString() + extension;
        String fullFileName = subDir + "/" + dateDir + "/" + fileName;
        
        // 保存文件
        File destFile = new File(dir, fileName);
        file.transferTo(destFile);
        
        log.info("文件上传成功：{}", fullFileName);
        return fullFileName;
    }
}