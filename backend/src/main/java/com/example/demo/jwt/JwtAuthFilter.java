package com.example.demo.jwt;

import com.example.demo.entity.AccountEntity;
import com.example.demo.repository.AccountRepository;
import com.example.demo.security.CustomUserDetailService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Optional;

@Component
public class JwtAuthFilter extends OncePerRequestFilter {

    private final JwtService jwtService;
    private final CustomUserDetailService customUserDetailService;
    private final AccountRepository accountRepository;

    public JwtAuthFilter(JwtService jwtService, CustomUserDetailService customUserDetailService,
                         AccountRepository accountRepository) {
        this.jwtService = jwtService;
        this.customUserDetailService = customUserDetailService;
        this.accountRepository = accountRepository;
    }

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain)
            throws ServletException, IOException {
        String token = jwtService.extractTokenFromRequest(request);
        if (token != null && jwtService.isValid(token, request)
                && SecurityContextHolder.getContext().getAuthentication() == null) {
            String email = jwtService.extractEmailFromToken(token);
            Optional<AccountEntity> account = accountRepository.getAccountEntityByEmailAndIsActive(email,true);
            if (!account.isPresent()) {
                request.setAttribute("userNotFoundByEmail", "Account not found by email is " + email + "!");
            } else {
                UserDetails userDetails = customUserDetailService.loadUserByUsername(email);
                UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(
                        userDetails,
                        null,
                        userDetails.getAuthorities());
                usernamePasswordAuthenticationToken
                        .setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
            }
        }
        else {
            request.setAttribute("TokenIsNull",true);
        }
        filterChain.doFilter(request, response);
    }
}
