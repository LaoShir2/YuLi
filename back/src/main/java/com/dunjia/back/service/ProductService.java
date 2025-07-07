package com.dunjia.back.service;

import com.dunjia.back.mapper.ProductMapper;
import com.dunjia.back.mapper.UserMapper;
import com.dunjia.back.pojo.Product;
import com.dunjia.back.pojo.User;
import jakarta.annotation.Resource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@Service
public class ProductService {
    @Autowired
    private ProductMapper productMapper;

    @Resource
    private UserMapper userMapper;

    public List<Product> getAllProducts() {
        return productMapper.getAllProducts();  // 使用Mapper接口获取产品数据
    }

    public List<Product> getSearchedProducts(String keyword) {
        return productMapper.getSearchedProducts(keyword);  // 使用Mapper接口获取搜索结果
    }

    public Product getProductById(Integer id) {
        return productMapper.getProductById(id);  // 使用Mapper接口获取产品数据
    }

    public void addProduct(Product product) {
        product.setCreateTime(LocalDateTime.now()); // 设置产品创建时间
        productMapper.addProduct(product);  // 使用Mapper接口添加产品
    }

    public void deleteProductById(Integer id) {
        productMapper.deleteProductById(id);  // 使用Mapper接口删除产品
    }

    public List<Product> getPublishedProducts(Integer id) {
        return productMapper.getPublishedProducts(id);  // 使用Mapper接口获取已发布产品
    }

    public List<Product> getProductsListByPriceFromH() {
        return productMapper.getProductsListByPriceFromH();
    }

    public List<Product> getProductsListByPriceFromL() {
        return productMapper.getProductsListByPriceFromL();
    }

    public List<Product> getProductsListByTime() {
        return productMapper.getProductsListByTime();
    }

    public List<Product> getProductsListByTimeWeek() {
        return productMapper.getProductsListByTimeWeek();
    }

    public List<Product> getProductsListByTimeMonth() {
        return productMapper.getProductsListByTimeMonth();
    }

    public List<Product> getProductsByWantList(Integer userId) {
        // Fetch user based on userId
        User user = userMapper.getUserById(userId);
        if (user == null) {
            return Collections.emptyList(); // Return empty list if user not found
        }

        // Get the user's wantList (comma-separated product IDs)
        String wantList = user.getWantList();
        if (wantList == null || wantList.isEmpty()) {
            return Collections.emptyList(); // Return empty list if no products in the wantList
        }

        // Split the wantList string into individual product IDs
        List<Product> products = new ArrayList<>();
        for (String id : wantList.split(",")) {
            try {
                // Convert the product ID string to Integer and fetch the product
                Product product = productMapper.getProductById(Integer.parseInt(id));
                if (product != null) {
                    products.add(product);
                }
            } catch (NumberFormatException e) {
                System.out.println("Invalid product ID: " + id);
            }
        }
        return products;
    }

    public void updateProduct(Product product) {
        productMapper.updateProduct(product);
    }
}