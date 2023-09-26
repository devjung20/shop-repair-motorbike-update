package com.example.demo.jwt;

import com.example.demo.constant.JwtConstant;
import com.example.demo.security.CustomUserDetail;
import io.jsonwebtoken.*;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Service
@Slf4j
public class JwtService {
    public String generateToken(Object principal) {
        Map<String, Object> map = new HashMap<>();
        CustomUserDetail customUserDetail = (CustomUserDetail) principal;
        map.put("username", customUserDetail.getAccount().getUserName());
        map.put("email", customUserDetail.getAccount().getEmail());
        map.put("role", customUserDetail.getAccount().getRole().toString());
        map.put("id", customUserDetail.getAccount().getId());
        return Jwts.builder()
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + JwtConstant.Expired_Token_Time))
                .setClaims(map)
                .setSubject(customUserDetail.getAccount().getEmail())
                .signWith(SignatureAlgorithm.HS512, JwtConstant.SECRET_KEY)
                .compact();
    }

    public String extractTokenFromRequest(HttpServletRequest request) {
        String header = request.getHeader(HttpHeaders.AUTHORIZATION);
        if (header != null && header.startsWith("Bearer")) {
            return header.substring(7);
        }
        return null;
    }

    public boolean isValid(String token, HttpServletRequest request) {
        try {
            Jwts.parser()
                    .setSigningKey(JwtConstant.SECRET_KEY)
                    .parseClaimsJws(token)
                    .getBody()
                    .getSubject();
            return true;
        } catch (ExpiredJwtException exception) {
            log.error("Expired JWT token");
            request.setAttribute("tokenError", "Expired JWT token");
        } catch (UnsupportedJwtException exception) {
            log.error("Unsupported JWT token");
            request.setAttribute("tokenError", "Unsupported JWT token");
        } catch (MalformedJwtException exception) {
            log.error("Invalid JWT token");
            request.setAttribute("tokenError", "Invalid JWT token");
        } catch (SignatureException exception) {
            log.error("Signature JWT token");
            request.setAttribute("tokenError", "Signature JWT token");
        }
        return false;
    }

    public String extractEmailFromToken(String token) {
        return Jwts.parser()
                .setSigningKey(JwtConstant.SECRET_KEY)
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }
}
