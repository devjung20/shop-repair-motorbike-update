package com.example.demo.controller;

import com.example.demo.model.BaseResponse;
import com.example.demo.model.request.ServiceRequest;
import com.example.demo.service.ServiceService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/services")
public class ServiceController {
    private final ServiceService service;

    public ServiceController(ServiceService service) {
        this.service = service;
    }

    @PreAuthorize("hasAuthority('MANAGER')")
    @PostMapping("")
    public ResponseEntity<BaseResponse> create(@Valid @RequestBody ServiceRequest serviceRequest) {
        BaseResponse response = service.create(serviceRequest);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

    @PreAuthorize("hasAnyAuthority('MANAGER','DISPATCHER')")
    @GetMapping("")
    public ResponseEntity<BaseResponse> getAll(
            @RequestParam Map<String, String> map) {
        BaseResponse response = service.getByParams(map);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

    @PreAuthorize("hasAnyAuthority('MANAGER','DISPATCHER')")
    @GetMapping("/{serviceId}")
    public ResponseEntity<BaseResponse> getById(@PathVariable long serviceId) {
        BaseResponse response = service.getById(serviceId);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

    @PreAuthorize("hasAuthority('MANAGER')")
    @PutMapping("/{serviceId}")
    public ResponseEntity<BaseResponse> update(@Valid @RequestBody ServiceRequest serviceRequest,
                                               @PathVariable long serviceId) {
        BaseResponse response = service.update(serviceRequest, serviceId);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

    @PreAuthorize("hasAuthority('MANAGER')")
    @DeleteMapping("/{serviceId}")
    public ResponseEntity<BaseResponse> delete(@PathVariable long serviceId) {
        BaseResponse response = service.deleteById(serviceId);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }
}
