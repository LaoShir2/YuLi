package com.dunjia.back.mapper;

import com.dunjia.back.pojo.Trade;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface TradeMapper {

    void addTrade(Trade newTrade);

    @Select("select * from trades")
    List<Trade> getAllTrades();

    @Select("select * from trades where productId = #{productId}")
    Trade getTradeByProductId(Integer productId);

    void updateTradeByProductId(Trade trade);

    @Delete("delete from trades where productId = #{productId}")
    void deleteTradeByProductId(Integer productId);

    @Delete("delete from trades where id = #{id}")
    void deleteTradeById(Integer id);
}
