package com.example.demo.controller;

import com.example.demo.model.BaseResponse;
import com.example.demo.model.request.CustomerRequest;
import com.example.demo.service.CustomerService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/customers")
public class CustomerController {
    private final CustomerService customerService;

    public CustomerController(CustomerService customerService) {
        this.customerService = customerService;
    }

    @GetMapping("/active")
    @PreAuthorize("hasAnyAuthority('MANAGER','DISPATCHER')")
    public ResponseEntity<BaseResponse> getAllCustomersActive(
            @RequestParam(name = "pageSize", defaultValue = "5") int pageSize,
            @RequestParam(name = "pageNumber", defaultValue = "1") int pageNumber) {
        BaseResponse response = customerService.getAll(pageNumber - 1, pageSize);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

    @GetMapping
    @PreAuthorize("hasAnyAuthority('MANAGER','DISPATCHER')")
    public ResponseEntity<BaseResponse> getAll(@RequestParam Map<String, String> params) {
        BaseResponse response = customerService.getByParams(params);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

    @GetMapping("/{customerId}")
    @PreAuthorize("hasAnyAuthority('MANAGER','DISPATCHER')")
    public ResponseEntity<BaseResponse> getCustomerById(@PathVariable Long customerId) {
        BaseResponse response = customerService.getById(customerId);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

    @PreAuthorize("hasAnyAuthority('MANAGER','DISPATCHER')")
    @PostMapping
    public ResponseEntity<BaseResponse> createCustomer(@Valid @RequestBody CustomerRequest customerRequest) {
        BaseResponse response = customerService.create(customerRequest);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

    @PreAuthorize("hasAnyAuthority('MANAGER','DISPATCHER')")
    @PutMapping("/{customerId}")
    public ResponseEntity<BaseResponse> updateCustomer(@Valid @RequestBody CustomerRequest customerRequest,
                                                       @PathVariable Long customerId) {
        BaseResponse response = customerService.update(customerRequest, customerId);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

    @PreAuthorize("hasAnyAuthority('MANAGER','DISPATCHER')")
    @DeleteMapping("/{customerId}")
    public ResponseEntity<BaseResponse> deleteCustomer(@PathVariable Long customerId) {
        BaseResponse response = customerService.deleteById(customerId);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

    @GetMapping("/search")
    @PreAuthorize("hasAnyAuthority('MANAGER','DISPATCHER')")
    public ResponseEntity<BaseResponse> searchByNameAndPhoneAndEmail(
            @RequestParam(name = "name", required = false) String name,
            @RequestParam(name = "phone", required = false) String phone,
            @RequestParam(name = "email", required = false) String email,
            @RequestParam(name = "orderBy", defaultValue = "nameAsc") String orderBy,
            @RequestParam(name = "pageNumber", defaultValue = "0") int pageNumber,
            @RequestParam(name = "pageSize", defaultValue = "5") int pageSize) {
        BaseResponse response = customerService.searchByNameAndPhoneAndEmail(pageNumber, pageSize, name, phone, email, orderBy);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }
}
