package com.dunjia.back.controller;

import com.dunjia.back.pojo.Result;
import com.dunjia.back.pojo.Trade;
import com.dunjia.back.service.TradeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
public class TradeController {
    @Autowired
    private TradeService tradeService;

    // 获取所有trade信息
    @GetMapping("/trades")
    public Result getAllTrades() {
        log.info("Get all trades");
        List<Trade> trades = tradeService.getAllTrades();
        return Result.success(trades);
    }

    // 获取trade信息通过productId
    @GetMapping("/trades/{productId}")
    public Result getTradeByProductId(@PathVariable Integer productId) {
        log.info("Get trade by productId: {}", productId);
        Trade trade = tradeService.getTradeByProductId(productId);
        return Result.success(trade);
    }

    // 更新trade信息
    @PutMapping("/trades/{productId}")
    public Result updateTradeByProductId(@PathVariable Integer productId) {
        log.info("Update trade by productId: {}", productId);
         tradeService.updateTradeByProductId(productId);
        return Result.success(null);
    }

    // 删除trade信息
    @DeleteMapping("/trades/{productId}")
    public Result deleteTradeByProductId(@PathVariable Integer productId) {
        log.info("Delete trade by productId: {}", productId);
        tradeService.deleteTradeByProductId(productId);
        return Result.success(null);
    }

}
