package com.example.demo.security;

import com.example.demo.entity.AccountEntity;
import com.example.demo.repository.AccountRepository;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailService implements UserDetailsService {

    private final AccountRepository accountRepository;

    public CustomUserDetailService(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        AccountEntity account = accountRepository.getAccountEntityByEmailAndIsActive(username,true).orElseThrow(() -> {
            throw new BadCredentialsException("Không tìm thấy tài khoản qua email.");
        });
        return new CustomUserDetail(account);
    }
}
