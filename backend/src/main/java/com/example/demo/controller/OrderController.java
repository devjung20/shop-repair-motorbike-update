package com.example.demo.controller;

import com.example.demo.model.BaseResponse;
import com.example.demo.model.request.InsertOrder;
import com.example.demo.service.OrderService;
import jakarta.validation.Valid;

import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/orders")
public class OrderController {

    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @PreAuthorize("hasAnyAuthority('MANAGER','DISPATCHER')")
    @PostMapping("")
    public ResponseEntity<BaseResponse> create(@Valid @RequestBody InsertOrder insertOrder) {
        BaseResponse response = orderService.create(insertOrder);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

    @PreAuthorize("hasAnyAuthority('MANAGER','DISPATCHER')")
    @GetMapping
    public ResponseEntity<BaseResponse> getAll(
            @RequestParam Map<String, String> map) {
        BaseResponse response = orderService.getByParams(map);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

    @PreAuthorize("hasAnyAuthority('MANAGER','DISPATCHER')")
    @GetMapping("/{orderId}")
    public ResponseEntity<BaseResponse> getById(@PathVariable long orderId) {
        BaseResponse response = orderService.getById(orderId);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

    @PreAuthorize("hasAnyAuthority('MANAGER','DISPATCHER')")
    @PutMapping("/{orderId}")
    public ResponseEntity<BaseResponse> update(@Valid @RequestBody InsertOrder insertOrder,
                                               @PathVariable long orderId) {
        BaseResponse response = orderService.update(insertOrder, orderId);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

    @PreAuthorize("hasAnyAuthority('MANAGER','DISPATCHER')")
    @DeleteMapping("/{orderId}")
    public ResponseEntity<BaseResponse> delete(@PathVariable long orderId) {
        BaseResponse response = orderService.deleteById(orderId);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

    @PreAuthorize("hasAnyAuthority('MANAGER','DISPATCHER')")
    @PutMapping("/toInvoice/{orderId}")
    public ResponseEntity<BaseResponse> toInvoice(@PathVariable long orderId) {
        BaseResponse response = orderService.toInvoice(orderId);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }
}
