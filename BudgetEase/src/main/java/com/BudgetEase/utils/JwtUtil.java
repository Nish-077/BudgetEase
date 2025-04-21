package com.BudgetEase.utils;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;

import io.github.cdimascio.dotenv.Dotenv;

public class JwtUtil {

    static Dotenv dotenv = Dotenv.load();

    private static final String SECRET = dotenv.get("JWT_SECRET");

    static {
        if (SECRET == null || SECRET.isEmpty()) {
            throw new IllegalStateException("JWT_SECRET environment variable is not set");
        }
    }

    public static DecodedJWT validateToken(String token) {
        try {
            // System.out.println("In JWT UTILS SECRET IS " + SECRET);
            Algorithm algorithm = Algorithm.HMAC256(SECRET);
            JWTVerifier verifier = JWT.require(algorithm).build();
            return verifier.verify(token);
        } catch (Exception e) {
            System.err.println("Token validation failed: " + e.getMessage());
            throw new IllegalArgumentException("Invalid or expired token");
        }
    }

    public static String getUserIdFromToken(String token) {
        DecodedJWT decodedJWT = validateToken(token);

        return decodedJWT.getSubject(); // Assuming userId is stored in the subject
    }
}