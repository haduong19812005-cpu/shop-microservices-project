package com.shop.product.apigateway.sercurity;

import io.jsonwebtoken.Jwts;

public class JwtUtil {

    private static final String SECRET_KEY = "my-secret-key";

    public static boolean validateToken(String token) {
        try {
            Jwts.parser()
                    .setSigningKey(SECRET_KEY.getBytes())
                    .parseClaimsJws(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}