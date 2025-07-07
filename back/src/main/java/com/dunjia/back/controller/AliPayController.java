package com.dunjia.back.controller;

import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.request.AlipayTradePagePayRequest;
import com.alipay.easysdk.factory.Factory;
import com.dunjia.back.config.AliPayConfig;
import com.dunjia.back.pojo.AliPay;
import com.dunjia.back.pojo.Trade;
import com.dunjia.back.service.TradeService;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@Transactional(rollbackFor = Exception.class)
@RequestMapping("/alipay")
public class AliPayController {

    @Resource
    AliPayConfig aliPayConfig;

    @Autowired
    private TradeService tradeService;

    private static final String GATEWAY_URL = "https://openapi-sandbox.dl.alipaydev.com/gateway.do";
    private static final String FORMAT = "JSON";
    private static final String CHARSET = "utf-8";
    private static final String SIGN_TYPE = "RSA2";

    @GetMapping("/pay") // 前端路径参数格式?subject=ExampleProduct&traceNo=123456&totalAmount=100.00&body=12,34
    public void pay(AliPay aliPay, HttpServletResponse httpResponse) throws Exception {
        AlipayClient alipayClient = new DefaultAlipayClient(GATEWAY_URL, aliPayConfig.getAppId(),
                aliPayConfig.getAppPrivateKey(), FORMAT, CHARSET, aliPayConfig.getAlipayPublicKey(), SIGN_TYPE);
        AlipayTradePagePayRequest request = new AlipayTradePagePayRequest();
        request.setNotifyUrl(aliPayConfig.getNotifyUrl());
        request.setReturnUrl(aliPayConfig.getReturnUrl());
        request.setBizContent("{\"out_trade_no\":\"" + aliPay.getTraceNo() + "\","
                + "\"total_amount\":\"" + aliPay.getTotalAmount() + "\","
                + "\"subject\":\"" + aliPay.getSubject() + "\","
                + "\"product_code\":\"FAST_INSTANT_TRADE_PAY\","
                + "\"body\":\"" + aliPay.getBody() + "\"}");
        String form = "";
        try {
            // 调用SDK生成表单
            form = alipayClient.pageExecute(request).getBody();
        } catch (AlipayApiException e) {
            e.printStackTrace();
        }
        httpResponse.setContentType("text/html;charset=" + CHARSET);
        // 直接将完整的表单html输出到页面
        httpResponse.getWriter().write(form);
        httpResponse.getWriter().flush();
        httpResponse.getWriter().close();
    }

    @PostMapping("/notify")  // 注意这里必须是POST接口
    public String payNotify(HttpServletRequest request) throws Exception {
        if (request.getParameter("trade_status").equals("TRADE_SUCCESS")) {
            System.out.println("=========支付宝异步回调========");

            Map<String, String> params = new HashMap<>();
            Map<String, String[]> requestParams = request.getParameterMap();
            for (String name : requestParams.keySet()) {
                params.put(name, String.join(",", requestParams.get(name)));
            }

            System.out.println("支付宝异步回调参数: " + params);

            // 支付宝验签
            if (Factory.Payment.Common().verifyNotify(params)) {
                // 验签通过
                System.out.println("验签通过，开始保存数据...");
                // 验签通过
                System.out.println("交易名称: " + params.get("subject"));
                System.out.println("交易状态: " + params.get("trade_status"));
                System.out.println("支付宝交易凭证号: " + params.get("trade_no"));
                System.out.println("商户订单号: " + params.get("out_trade_no"));
                System.out.println("交易金额: " + params.get("total_amount"));
                System.out.println("买家在支付宝唯一id: " + params.get("buyer_id"));
                System.out.println("买家付款时间: " + params.get("gmt_payment"));
                System.out.println("买家付款金额: " + params.get("buyer_pay_amount"));

                // 更新订单已支付的逻辑代码
                // 在notify接口中的支付宝验签通过后，编写支付成功的逻辑。例如：修改订单状态、更新菜品销量、清空购物车等等。
                Trade newTrade = new Trade();
                newTrade.setTotalAmount(Double.parseDouble(params.get("total_amount")));
                newTrade.setProductName(params.get("subject"));
                newTrade.setCreateTime(java.time.LocalDateTime.now());
                newTrade.setStatus(2); // 2表示已支付
                // body: "sellerId, buyerId"
                String[] body = params.get("body").split(",");
                newTrade.setSellerId(Integer.parseInt(body[0]));
                newTrade.setBuyerId(Integer.parseInt(body[1]));
//                newTrade.setSellerId(1);
//                newTrade.setBuyerId(1);
                // traceNo: productId + '-' + UUID
                newTrade.setProductId(Integer.parseInt(params.get("out_trade_no").split("-")[0]));
                // add to database
                tradeService.addTrade(newTrade);
            }
        }
        return "success";
    }

}