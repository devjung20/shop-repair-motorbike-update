package com.example.demo.controller;

import com.example.demo.model.BaseResponse;
import com.example.demo.model.request.StoreRequest;
import com.example.demo.service.StoreService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "api/stores")
@PreAuthorize("hasAuthority('MANAGER')")
public class StoreController {
    private final StoreService service;

    public StoreController(StoreService service) {
        this.service = service;
    }

    @PostMapping()
    public ResponseEntity<BaseResponse> createOrUpdate(@RequestBody StoreRequest storeRequest) {
        return ResponseEntity.ok(service.create(storeRequest));
    }

    @GetMapping()
    public ResponseEntity<BaseResponse> getInfoStore() {
        return ResponseEntity.ok(service.getByParams(null));
    }
}
