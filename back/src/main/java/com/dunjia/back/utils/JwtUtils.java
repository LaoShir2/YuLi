package com.dunjia.back.utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import java.util.Date;
import java.util.Map;

public class JwtUtils {

    // 从环境变量获取密钥
    private static final String signKey = "secret";
    private static final Long expire = 30 * 60 * 1000L;  // token 过期时间 30 分钟

    // 生成 jwt
    public static String generateJwt() {
        return Jwts.builder()
                .signWith(SignatureAlgorithm.HS256, signKey)
                .setExpiration(new Date(System.currentTimeMillis() + expire))  // 设置过期时间
                .compact();
    }

    // 解析 jwt
    public static Claims parseJwt(String jwt) {
        try {
            return Jwts.parser()
                    .setSigningKey(signKey)
                    .parseClaimsJws(jwt)
                    .getBody();
        } catch (Exception e) {
            throw new RuntimeException("无效的JWT token");
        }
    }

}
