package com.dunjia.back.service;

import com.dunjia.back.mapper.TradeMapper;
import com.dunjia.back.pojo.Product;
import com.dunjia.back.pojo.Trade;
import jakarta.annotation.Resource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TradeService {
    @Autowired
    private TradeMapper tradeMapper;

    @Resource
    private ProductService productService;

    public void addTrade(Trade newTrade) {
        tradeMapper.addTrade(newTrade);
        Product product = productService.getProductById(newTrade.getProductId());
        product.setStatus(2);
        product.setSellerId(newTrade.getSellerId());
        product.setBuyerId(newTrade.getBuyerId());
        productService.updateProduct(product);
    }

    public List<Trade> getAllTrades() {
        return tradeMapper.getAllTrades();
    }

    public void updateTradeByProductId(Integer productId) {
        System.out.println("Update trade by productId: " + productId);
        Trade trade = tradeMapper.getTradeByProductId(productId);
        if (trade == null) {
            return;
        }
        Product product = productService.getProductById(productId);
        trade.setSellerId(product.getSellerId());
        trade.setBuyerId(product.getBuyerId());
        tradeMapper.updateTradeByProductId(trade);
    }

    public Trade getTradeByProductId(Integer productId) {
        return tradeMapper.getTradeByProductId(productId);
    }

    public void deleteTradeByProductId(Integer productId) {
        tradeMapper.deleteTradeByProductId(productId);
    }
}
