package com.example.demo.controller;

import com.example.demo.model.BaseResponse;
import com.example.demo.model.request.AccountRequest;
import com.example.demo.model.response.BaseAccount;
import com.example.demo.service.AccountService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping(value = "/api/accounts")
public class AccountController {

    private final AccountService accountService;

    public AccountController(AccountService accountService) {
        this.accountService = accountService;
    }

    @PreAuthorize("hasAuthority('MANAGER')")
    @PutMapping("/{accountId}")
    public ResponseEntity<BaseResponse> updateAccount(@Valid @RequestBody AccountRequest accountRequest,
            @PathVariable long accountId) {
        BaseResponse response = accountService.update(accountRequest, accountId);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

    @PreAuthorize("hasAuthority('MANAGER')")
    @DeleteMapping("/{accountId}")
    public ResponseEntity<BaseResponse> deleteAccount(@PathVariable long accountId) {
        BaseResponse response = accountService.deleteById(accountId);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }
    @PreAuthorize("hasAuthority('MANAGER')")
    @GetMapping("/{accountId}")
    public ResponseEntity<BaseResponse> findById(@PathVariable long accountId) {
        BaseResponse response = accountService.getById(accountId);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }
    @PreAuthorize("hasAuthority('MANAGER')")
    @GetMapping("")
    public ResponseEntity<BaseResponse> getAllAccount(
            @RequestParam Map<String, String> map) {
        BaseResponse response = accountService.getByParams(map);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }
    @PreAuthorize("hasAuthority('MANAGER')")
    @GetMapping("/search")
    public ResponseEntity<BaseResponse> searchByManyConditions(
            @RequestBody BaseAccount baseAccount,
            @RequestParam(name = "pageSize", defaultValue = "5") int pageSize,
            @RequestParam(name = "pageNumber", defaultValue = "1") int pageNumber) {
        BaseResponse response = this.accountService.searchByManyConditions(baseAccount, pageNumber - 1, pageSize);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }
}
