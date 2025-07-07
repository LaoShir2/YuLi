package com.dunjia.back.service;

import com.dunjia.back.mapper.ProductMapper;
import com.dunjia.back.mapper.TradeMapper;
import com.dunjia.back.mapper.UserMapper;
import com.dunjia.back.pojo.Product;
import com.dunjia.back.pojo.Trade;
import com.dunjia.back.pojo.User;
import jakarta.annotation.Resource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserService {
    @Autowired
    private UserMapper userMapper;
    @Resource
    private ProductMapper productMapper;
    @Resource
    private TradeMapper tradeMapper;

    public User login(User user) {
        return userMapper.getByUserNameAndPassword(user);
    }

    public List<User> getAllUsers() {
        return userMapper.getAllUsers();
    }

    public User getUserById(Integer id) {
        return userMapper.getUserById(id);
    }

    public void addUser(User user) {
        userMapper.addUser(user);
    }

    public void deleteUserById(Integer id) {
        List<Product> products = productMapper.getAllProducts();
        for (Product product : products) {
            if (product.getSellerId().equals(id)) {
                productMapper.deleteProductById(product.getId());
            }
        }
        List<Trade> trades = tradeMapper.getAllTrades();
        for (Trade trade : trades) {
            if (trade.getBuyerId().equals(id) || trade.getSellerId().equals(id)) {
                tradeMapper.deleteTradeById(trade.getId());
            }
        }
        userMapper.deleteUserById(id);
    }

    public void toggleProductInWantList(Integer userId, Integer productId) {
        System.out.println("userId: " + userId + " productId: " + productId);

        // Fetch user object from the database
        User user = userMapper.getUserById(userId);
        System.out.println("user: " + user);

        // Get the current want list
        String wantList = user.getWantList();

        // Initialize a StringBuilder to build the updated want list
        StringBuilder updatedWantList = new StringBuilder();

        // Check if wantList is empty or null
        if (wantList == null || wantList.isEmpty()) {
            // If the list is empty, add the productId to the list
            updatedWantList.append(productId);
        } else {
            // Split the current wantList by commas to get individual product IDs
            String[] productIds = wantList.split(",");
            boolean isProductIdPresent = false;

            // Loop through the product IDs to check if the productId is already in the list
            for (String id : productIds) {
                if (id.equals(String.valueOf(productId))) {
                    // If the productId is found, don't add it again (remove it)
                    isProductIdPresent = true;
                } else {
                    // If the productId is not the one we want to toggle, retain it in the updated list
                    if (updatedWantList.length() > 0) {
                        updatedWantList.append(","); // Add a comma if not the first element
                    }
                    updatedWantList.append(id);
                }
            }

            // If the productId was not found, we need to add it
            if (!isProductIdPresent) {
                if (updatedWantList.length() > 0) {
                    updatedWantList.append(","); // Add a comma before appending new productId
                }
                updatedWantList.append(productId);
            }
        }

        // If the productId was present and removed, updatedWantList will now exclude it.
        // If the productId was not present, updatedWantList will now include it.

        // Update the wantList in the user object
        user.setWantList(updatedWantList.toString());

        // Update the user in the database
        userMapper.updateUser(user);
    }


    public List<Integer> getWantList(Integer userId) {
        User user = userMapper.getUserById(userId);
        String wantList = user.getWantList();
        List<Integer> wantListIds = new ArrayList<>();

        if (wantList != null && !wantList.isEmpty()) {
            String[] productIds = wantList.split(",");
            for (String id : productIds) {
                wantListIds.add(Integer.parseInt(id));
            }
        }

        return wantListIds;
    }

    public void updateUser(User user) {
        userMapper.updateUser(user);
    }
}
