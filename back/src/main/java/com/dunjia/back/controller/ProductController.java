package com.dunjia.back.controller;

import com.dunjia.back.pojo.Product;
import com.dunjia.back.pojo.Result;
import com.dunjia.back.service.ProductService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
public class ProductController {
    @Autowired
    private ProductService productService;

    // 获取所有product信息
    @GetMapping("/products")
    public Result getAllProducts() {
        log.info("Get all products");
        List<Product> products = productService.getAllProducts();
        return Result.success(products);
    }
    //按价格对商品排序高到低
    @GetMapping("/products/Higher")
    public Result getProductsListByPriceFromH() {
        log.info("Get products list by price from h");
        List<Product> products = productService.getProductsListByPriceFromH();
        return Result.success(products);
    }
    //按价格对商品排序低到高
    @GetMapping("/products/Lower")
    public Result getProductsListByPriceFromL() {
        log.info("get products list by price from l");
        List<Product> products = productService.getProductsListByPriceFromL();
        return Result.success(products);
    }
    //展示商品按时间
    @GetMapping("/products/ByTime")
    public Result getProductsListByTime() {
        log.info("");
        List<Product> products = productService.getProductsListByTime();
        return Result.success(products);
    }
    @GetMapping("/products/ByTimeWeek")
    public Result getProductsListByTimeWeek() {
        log.info("");
        List<Product> products = productService.getProductsListByTimeWeek();
        return Result.success(products);
    }
    @GetMapping("/products/ByTimeMonth")
    public Result getProductsListByTimeMonth() {
        log.info("");
        List<Product> products = productService.getProductsListByTimeMonth();
        return Result.success(products);
    }
    // 根据id获取product信息
    @GetMapping("/products/{id}")
    public Result getProductById(@PathVariable Integer id) {
        log.info("Get product by id: {}", id);
        Product product = productService.getProductById(id);
        return Result.success(product);
    }

    // 根据sellerId获取product信息
    @GetMapping("/products/profile/{sellerId}")
    public Result getProductsBySellerId(@PathVariable Integer sellerId) {
        log.info("Get products by seller id: {}", sellerId);
        List<Product> products = productService.getPublishedProducts(sellerId);
        return Result.success(products);
    }

    // 根据关键字搜索product信息
    @GetMapping("/products/search")
    public Result getSearchedProducts(@RequestParam(defaultValue = "") String keyword) {
        log.info("Get searched products with keyword: {}", keyword);
        List<Product> products = productService.getSearchedProducts(keyword);
        return Result.success(products);
    }

    // 添加product
    @PostMapping("/products")
    public Result addProduct(@RequestBody Product product) {
        log.info("Add product: {}", product);
        productService.addProduct(product);
        return Result.success(null);
    }

    // 根据userId获取product信息
    @GetMapping("/products/wantList/{userId}")
    public Result getProductsByWantList(@PathVariable Integer userId) {
        log.info("Get products by wantList: userId={}", userId);
        List<Product> products = productService.getProductsByWantList(userId);
        return Result.success(products);
    }

    // 根据id删除product
    @DeleteMapping("/products/{id}")
    public Result deleteProductById(@PathVariable Integer id) {
        log.info("delete product by id: {}", id);
        productService.deleteProductById(id);
        return Result.success(null);
    }

    // 更新product信息
    @PutMapping("/products")
    public Result updateProductById(@RequestBody Product product) {
        log.info("update product by id: {}", product.getId());
        productService.updateProduct(product);
        return Result.success(null);
    }

}