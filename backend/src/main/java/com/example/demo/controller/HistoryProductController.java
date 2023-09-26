package com.example.demo.controller;

import com.example.demo.model.BaseResponse;
import com.example.demo.model.request.HistoryProductRequest;
import com.example.demo.service.HistoryProductService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/history")
public class HistoryProductController {
    private final HistoryProductService historyProductService;

    public HistoryProductController(HistoryProductService historyProductService) {
        this.historyProductService = historyProductService;
    }
    @PreAuthorize("hasAnyAuthority('MANAGER','DISPATCHER')")
    @GetMapping()
    public ResponseEntity<BaseResponse> getAllHistoryProducts(
            @RequestParam Map<String,String > map) {
        BaseResponse response = historyProductService.getHistoryProductByConditons(map);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }
    @PreAuthorize("hasAnyAuthority('MANAGER','DISPATCHER')")
    @GetMapping("/search")
    public ResponseEntity<BaseResponse> searchByNameAndAction(
            @RequestParam(name = "productName", required = false) String productName,
            @RequestParam(name = "action", required = false) String action,
            @RequestParam(name = "orderBy", defaultValue = "productNameAsc") String orderBy,
            @RequestParam(name = "pageNumber", defaultValue = "0") int pageNumber,
            @RequestParam(name = "pageSize", defaultValue = "5") int pageSize) {
        BaseResponse response = historyProductService.searchByNameAndAction(pageNumber, pageSize, productName, action, orderBy);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }
    @PreAuthorize("hasAuthority('MANAGER')")
    @PostMapping()
    public ResponseEntity<BaseResponse> createHistory(
            @Valid @RequestBody HistoryProductRequest historyProductRequest) {
        BaseResponse response = historyProductService.createHistory(historyProductRequest , true,true);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }
}
