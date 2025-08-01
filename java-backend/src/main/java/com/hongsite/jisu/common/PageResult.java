package com.hongsite.jisu.common;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 分页响应结果类
 * 
 * @author 鸿思特科技有限公司
 */
@Data
@Schema(description = "分页响应结果")
public class PageResult<T> implements Serializable {
    
    private static final long serialVersionUID = 1L;

    @Schema(description = "数据列表")
    private List<T> list;

    @Schema(description = "总记录数")
    private Long total;

    @Schema(description = "当前页码")
    private Long current;

    @Schema(description = "每页大小")
    private Long size;

    @Schema(description = "总页数")
    private Long pages;

    public PageResult() {}

    public PageResult(List<T> list, Long total, Long current, Long size) {
        this.list = list;
        this.total = total;
        this.current = current;
        this.size = size;
        this.pages = (total + size - 1) / size;
    }

    /**
     * 构建分页结果
     */
    public static <T> PageResult<T> build(List<T> list, Long total, Long current, Long size) {
        return new PageResult<>(list, total, current, size);
    }
}