package com.example.demo.controller;

import com.example.demo.model.BaseResponse;
import com.example.demo.model.request.EmployeeRequest;
import com.example.demo.service.EmployeeService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/employees")
public class EmployeeController {

    private final EmployeeService employeeService;

    public EmployeeController(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }
    @PreAuthorize("hasAuthority('MANAGER')")
    @PostMapping
    public ResponseEntity<BaseResponse> createEmployee(@Valid @RequestBody EmployeeRequest employeeRequest) {
        BaseResponse response = employeeService.create(employeeRequest);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }
    @PreAuthorize("hasAnyAuthority('MANAGER','DISPATCHER')")
    @GetMapping("/{employeeId}")
    public ResponseEntity<BaseResponse> findById(@PathVariable long employeeId) {
        BaseResponse response = employeeService.getById(employeeId);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

    // @GetMapping("")
    // public ResponseEntity<BaseResponse> getAll(
    // @RequestParam(name = "pageNumber", defaultValue = "1") int pageNumber,
    // @RequestParam(name = "pageSize", defaultValue = "5") int pageSize) {
    // return ResponseEntity.ok(employeeService.getAll(pageNumber - 1, pageSize));
    // }

    @PreAuthorize("hasAuthority('MANAGER')")
    @PutMapping("/{employeeId}")
    public ResponseEntity<BaseResponse> update(@Valid @RequestBody EmployeeRequest employeeRequest,
                                               @PathVariable long employeeId) {
        BaseResponse response = employeeService.update(employeeRequest, employeeId);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }
    @PreAuthorize("hasAuthority('MANAGER')")
    @DeleteMapping("/{employeeId}")
    public ResponseEntity<BaseResponse> delete(@PathVariable long employeeId) {
        BaseResponse response = employeeService.deleteById(employeeId);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }
    @PreAuthorize("hasAnyAuthority('MANAGER','DISPATCHER')")
    @GetMapping("/active")
    public ResponseEntity<BaseResponse> getAllActive(
            @RequestParam(name = "pageNumber", defaultValue = "1") int pageNumber,
            @RequestParam(name = "pageSize", defaultValue = "5") int pageSize) {
        BaseResponse response = employeeService.getAllActive(pageNumber - 1, pageSize);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }
    @PreAuthorize("hasAnyAuthority('MANAGER','DISPATCHER')")
    @GetMapping
    public ResponseEntity<BaseResponse> getAll(
            // @RequestParam(name = "pageNumber", defaultValue = "1") String pageNumber,
            // @RequestParam(name = "pageSize", defaultValue = "10") String pageSize,
            @RequestParam Map<String, String> params) {
        // return ResponseEntity.ok(employeeService.getAll(pageNumber - 1, pageSize));
        BaseResponse response = employeeService.getByParams(params);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }
    @PreAuthorize("hasAnyAuthority('MANAGER','DISPATCHER')")
    @GetMapping("/getEmployeeNoAccount")
    public ResponseEntity<BaseResponse> getEmployeeNoAccount() {
        BaseResponse response = employeeService.getEmployeeNoAccount();
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

}
