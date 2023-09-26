package com.example.demo.controller;

import com.example.demo.entity.ImageEntity;
import com.example.demo.exception.EntityNotFoundException;
import com.example.demo.model.BaseResponse;
import com.example.demo.model.request.ProductRequest;
import com.example.demo.repository.ImageRepository;
import com.example.demo.service.ProductService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/products")
public class ProductController {
    private final ProductService productService;

    private final ImageRepository imageRepository;

    public ProductController(ProductService productService, ImageRepository imageRepository) {
        this.productService = productService;
        this.imageRepository = imageRepository;
    }

    @PreAuthorize("hasAnyAuthority('MANAGER','DISPATCHER')")
    @GetMapping
    public ResponseEntity<BaseResponse> getAllProducts(
            @RequestParam Map<String, String> params) {
        return ResponseEntity.ok(productService.getByParams(params));
    }

    @PreAuthorize("hasAnyAuthority('MANAGER','DISPATCHER')")
    @GetMapping("/{productId}")
    public ResponseEntity<BaseResponse> getProductById(@PathVariable Long productId) {
        BaseResponse response = productService.getById(productId);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

    @PreAuthorize("hasAuthority('MANAGER')")
    @PostMapping
    public ResponseEntity<BaseResponse> createProduct(@Valid @RequestBody ProductRequest productRequest) {
        BaseResponse response = productService.create(productRequest);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

    @PreAuthorize("hasAuthority('MANAGER')")
    @PutMapping("/{productId}")
    public ResponseEntity<BaseResponse> updateProduct(@PathVariable Long productId,
            @Valid @RequestBody ProductRequest productRequest) {

        BaseResponse response = productService.update(productRequest, productId);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

    @PreAuthorize("hasAuthority('MANAGER')")
    @DeleteMapping("/{productId}")
    public ResponseEntity<BaseResponse> deleteProduct(@PathVariable Long productId) {
        BaseResponse response = productService.deleteById(productId);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

    // upload ảnh trước ở đây
    @PreAuthorize("hasAuthority('MANAGER')")
    @PostMapping("/upload")
    public ResponseEntity<BaseResponse> uploadImage(@RequestParam("file") MultipartFile image) throws IOException {
        BaseResponse response = productService.uploadImage(image);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

    @GetMapping("/images/{imageId}")
    public ResponseEntity<byte[]> getImageFromId(@PathVariable long imageId) {
        ImageEntity imageEntity = imageRepository.findById(imageId).orElseThrow(() -> {
            throw new EntityNotFoundException("Image", "Id", String.valueOf(imageId));
        });
        return ResponseEntity.status(HttpStatus.OK)
                .contentType(MediaType.parseMediaType(imageEntity.getType()))
                .body(imageEntity.getData());
    }

    @PreAuthorize("hasAuthority('MANAGER')")
    @PutMapping("/upload/{imageId}")
    public ResponseEntity<BaseResponse> updateImage(@RequestParam("file") MultipartFile file,
            @PathVariable long imageId) {
        BaseResponse response = productService.updateImage(file, imageId);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }
}
