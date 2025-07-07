package com.dunjia.back;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.Console;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@SpringBootTest
class BackApplicationTests {

	@Test
	void contextLoads() {
	}

	@Test
	void testGenJwt() {
		Map<String, Object> claims = new HashMap<>();
		claims.put("id", 1);
		claims.put("name", "admin");

		String jwt = Jwts.builder()
				.signWith(SignatureAlgorithm.HS256, "secret") // 签名算法和密钥
				.setClaims(claims) // 自定义属性(负载)
				.setExpiration(new Date(System.currentTimeMillis() + 3600 * 1000)) // 过期时间1小时
				.compact(); // 生成 JWT

		System.out.println(jwt);
	}

}
