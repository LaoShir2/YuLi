package com.dunjia.back.mapper;

import com.dunjia.back.pojo.Product;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface ProductMapper {

    @Select("select * from products where id = #{id}")
    Product getProductById(int id);

    @Select("select * from products")
    List<Product> getAllProducts();

    @Select("select * from products where name like concat('%', #{keyword}, '%')")
    List<Product> getSearchedProducts(String keyword);

    void addProduct(Product product);

    @Delete("delete from products where id = #{id}")
    void deleteProductById(Integer id);

    void updateProduct(Product product);

    @Select("select * from products where sellerId = #{id}")
    List<Product> getPublishedProducts(Integer id);

    @Select("select * from products order by price desc")
    List<Product> getProductsListByPriceFromH();

    @Select("select * from products order by price ")
    List<Product> getProductsListByPriceFromL();

    @Select("select * from products where createtime >= date_sub(now(), interval 1 day)")
    List<Product> getProductsListByTime();

    @Select("select * from products where createtime >= date_sub(now(), interval 1 week)")
    List<Product> getProductsListByTimeWeek();

    @Select("select * from products where createtime >= date_sub(now(), interval 1 month)")
    List<Product> getProductsListByTimeMonth();

}