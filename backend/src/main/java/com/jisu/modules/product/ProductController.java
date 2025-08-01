package com.jisu.modules.product;

import com.jisu.common.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
@CrossOrigin
public class ProductController {

    private final ProductService productService;

    @GetMapping
    public ApiResponse<Page<Product>> list(@RequestParam(defaultValue = "0") int page,
                                           @RequestParam(defaultValue = "20") int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createdAt"));
        return ApiResponse.ok(productService.list(pageable));
    }

    @GetMapping("/{id}")
    public ApiResponse<Product> get(@PathVariable String id) {
        return ApiResponse.ok(productService.get(id));
    }

    @PostMapping
    public ApiResponse<Product> add(@Validated @RequestBody Product product) {
        return ApiResponse.ok(productService.save(product));
    }

    @PutMapping("/{id}")
    public ApiResponse<Product> update(@PathVariable String id, @RequestBody Product product) {
        product.setId(id);
        return ApiResponse.ok(productService.save(product));
    }

    @DeleteMapping("/{id}")
    public ApiResponse<String> delete(@PathVariable String id) {
        productService.delete(id);
        return ApiResponse.ok("deleted");
    }
}