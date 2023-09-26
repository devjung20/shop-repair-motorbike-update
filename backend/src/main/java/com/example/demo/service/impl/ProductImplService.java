package com.example.demo.service.impl;

import com.example.demo.entity.ImageEntity;
import com.example.demo.entity.ProductEntity;
import com.example.demo.exception.BaseException;
import com.example.demo.exception.EntityNotFoundException;
import com.example.demo.model.BaseResponse;
import com.example.demo.model.params.ProductParams;
import com.example.demo.model.request.HistoryDetail;
import com.example.demo.model.request.HistoryProductRequest;
import com.example.demo.model.request.ProductRequest;
import com.example.demo.model.response.AccountCreateAndUpdate;
import com.example.demo.model.response.ImageResponse;
import com.example.demo.model.response.ProductResponse;
import com.example.demo.repository.AccountRepository;
import com.example.demo.repository.ImageRepository;
import com.example.demo.repository.ProductRepository;
import com.example.demo.service.HistoryProductService;
import com.example.demo.service.ProductService;
import com.google.common.base.CaseFormat;

import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Slf4j
public class ProductImplService implements ProductService {
    private final ProductRepository productRepository;
    private final AccountRepository accountRepository;
    private final HistoryProductService historyProductService;
    private final ImageRepository imageRepository;

    List<String> imageTypes = List.of("image/png", "image/jpeg", "image/jpg");

    public ProductImplService(ProductRepository productRepository, AccountRepository accountRepository,
            HistoryProductService historyProductService, ImageRepository imageRepository) {
        this.productRepository = productRepository;
        this.accountRepository = accountRepository;
        this.historyProductService = historyProductService;
        this.imageRepository = imageRepository;
    }

    @Override
    public BaseResponse getAll(int pageNumber, int pageSize) {
        Pageable pageable = PageRequest.of(pageNumber, pageSize);
        Page<ProductEntity> productPage = productRepository.findAll(pageable);
        List<ProductResponse> productResponses = productPage.getContent().stream().map(product -> {
            return this.entityToResponse(product, null, null);
        }).collect(Collectors.toList());
        Page<ProductResponse> productResponsePage = new PageImpl<>(productResponses, pageable,
                productPage.getTotalElements());
        return BaseResponse.builder()
                .message("Nhận tất cả danh sách sản phẩm thành công!")
                .statusCode(HttpStatus.OK.value())
                .data(productResponsePage)
                .build();
    }

    @Override
    @Transactional
    public BaseResponse create(ProductRequest productRequest) {
        try {
            ImageEntity imageEntity = null;
            if (productRequest.getImageId() != null) {
                imageEntity = imageRepository.findById(productRequest.getImageId()).orElseThrow(() -> {
                    throw new EntityNotFoundException("Image", "Id", String.valueOf(productRequest.getImageId()));
                });
            }
            ProductEntity productEntity = ProductEntity.builder()
                    .name(productRequest.getName())
                    .priceIn(productRequest.getPriceIn())
                    .priceOut(productRequest.getPriceOut())
                    .brand(productRequest.getBrand())
                    .description(productRequest.getDescription())
                    .storageQuantity(productRequest.getStorageQuantity())
                    .quantityWarning(productRequest.getQuantityWarning())
                    .unit(productRequest.getUnit())
                    .imageUrl(imageEntity != null ? imageEntity.getUrl() : null)
                    .build();
            productEntity.setIsActive(true);
            productEntity.setQuantityWarning(productRequest.getQuantityWarning());
            ProductEntity savedProduct = productRepository.save(productEntity);
            if (imageEntity != null) {
                imageEntity.setProductId(savedProduct.getId());
                imageRepository.save(imageEntity);
            }
            HistoryProductRequest historyProductRequest = HistoryProductRequest.builder()
                    .note("Khởi tạo linh kiện")
                    .historyDetails(
                            List.of(HistoryDetail
                                    .builder()
                                    .productId(savedProduct.getId())
                                    .difference(savedProduct.getStorageQuantity())
                                    .build()))
                    .build();
            historyProductService.createHistory(historyProductRequest, false, true);
            return BaseResponse.builder()
                    .statusCode(HttpStatus.CREATED.value())
                    .message("Tạo sản phẩm thành công.")
                    .data(this.entityToResponse(savedProduct, null, null))
                    .build();
        } catch (Exception e) {
            return BaseResponse.builder()
                    .statusCode(HttpStatus.INTERNAL_SERVER_ERROR.value())
                    .message("Tạo sản phẩm không thành công!")
                    .data(null)
                    .build();
        }
    }

    @Override
    @Transactional()
    public BaseResponse update(ProductRequest productRequest, Long aLong) {
        ImageEntity imageEntity = null;
        if (productRequest.getImageId() != null) {
            imageEntity = imageRepository.findById(productRequest.getImageId()).orElseThrow(() -> {
                throw new EntityNotFoundException("Image", "Id", String.valueOf(productRequest.getImageId()));
            });
        }
        ProductEntity optionalProduct = productRepository.findById(aLong).orElseThrow(() -> {
            throw new BaseException("Sản phẩm không tồn tại.");
        });
        ProductEntity existingProduct = optionalProduct;
        imageRepository.deleteAllByProductId(existingProduct.getId(), productRequest.getImageId());
        existingProduct.setName(productRequest.getName());
        existingProduct.setPriceIn(productRequest.getPriceIn());
        existingProduct.setPriceOut(productRequest.getPriceOut());
        existingProduct.setBrand(productRequest.getBrand());
        existingProduct.setDescription(productRequest.getDescription());
        existingProduct.setStorageQuantity(productRequest.getStorageQuantity());
        existingProduct.setQuantityWarning(productRequest.getQuantityWarning());
        existingProduct.setUnit(productRequest.getUnit());
        existingProduct.setImageUrl(imageEntity != null ? imageEntity.getUrl() : null);
        ProductEntity updateProduct = productRepository.save(existingProduct);
        if (imageEntity != null) {
            imageEntity.setProductId(updateProduct.getId());
            imageRepository.save(imageEntity);
        }
        return BaseResponse.builder()
                .statusCode(HttpStatus.OK.value())
                .message("Cập nhật sản phẩm thành công!")
                .data(this.entityToResponse(updateProduct, null, null))
                .build();
    }

    @Override
    public BaseResponse deleteById(Long aLong) {
        try {
            Optional<ProductEntity> optionalProduct = productRepository.findById(aLong);
            if (optionalProduct.isPresent()) {
                ProductEntity productEntity = optionalProduct.get();
                productEntity.setIsActive(false);
                productRepository.save(productEntity);
                return BaseResponse.builder()
                        .statusCode(HttpStatus.OK.value())
                        .message("Xóa sản phẩm thành công")
                        .data(null)
                        .build();
            } else {
                return BaseResponse.builder()
                        .statusCode(HttpStatus.NOT_FOUND.value())
                        .message("Sản phẩm không tồn tại.")
                        .data(null)
                        .build();
            }
        } catch (Exception e) {
            return BaseResponse.builder()
                    .statusCode(HttpStatus.INTERNAL_SERVER_ERROR.value())
                    .message("Xóa sản phẩm không thành công!")
                    .data(null)
                    .build();
        }
    }

    @Override
    public BaseResponse getById(Long aLong) {
        try {
            Optional<ProductEntity> optionalProduct = productRepository.findById(aLong);
            if (optionalProduct.isPresent()) {
                ProductEntity productEntity = optionalProduct.get();

                return BaseResponse.builder()
                        .statusCode(HttpStatus.OK.value())
                        .message("Sản phẩm được lấy thành công")
                        .data(this.entityToResponse(productEntity, null, null))
                        .build();
            } else {
                return BaseResponse.builder()
                        .statusCode(HttpStatus.NOT_FOUND.value())
                        .message("Sản phẩm không tồn tại.")
                        .data(null)
                        .build();
            }
        } catch (Exception e) {
            return BaseResponse.builder()
                    .statusCode(HttpStatus.INTERNAL_SERVER_ERROR.value())
                    .message("Sản phẩm không tồn tại!")
                    .data(null)
                    .build();
        }
    }

    @Override
    public BaseResponse uploadImage(MultipartFile image) throws IOException {
        // imageRepository.deleteAllImageHasProductIdIsNull();
        imageRepository.deleteByProductIdIsNull();

        if (!imageTypes.contains(image.getContentType())) {
            throw new BaseException("Tệp không được phép");
        }
        ImageEntity imageEntity = ImageEntity.builder()
                .type(image.getContentType())
                .data(image.getBytes())
                .build();
        ImageEntity imageEntitySaved = imageRepository.save(imageEntity);
        imageEntitySaved.setUrl(generateUrlFromImage(imageEntitySaved));
        imageRepository.save(imageEntitySaved);
        return BaseResponse.builder()
                .data(ImageResponse.builder()
                        .id(imageEntitySaved.getId())
                        .url(imageEntitySaved.getUrl())
                        .build())
                .statusCode(HttpStatus.CREATED.value())
                .message("Tải hình ảnh lên thành công.")
                .build();
    }

    private String generateUrlFromImage(ImageEntity imageEntitySaved) {
        return ServletUriComponentsBuilder
                .fromCurrentContextPath()
                .path("/api/products/images/")
                .path(imageEntitySaved.getId().toString())
                .toUriString();
    }

    @Override
    @Transactional
    public void addToInventory(Long productId, int difference, String note) {
        ProductEntity product = productRepository.findById(productId).orElseThrow(() -> {
            throw new EntityNotFoundException("Product", "Id", productId.toString());
        });

        if (product != null) {
            product.addToInventory(difference, note);
            productRepository.save(product);
        }
    }

    @Override
    @Transactional
    public void removeFromInventory(Long productId, int difference, String note) {
        ProductEntity product = productRepository.findById(productId).orElseThrow(() -> {
            throw new EntityNotFoundException("Product", "Id", productId.toString());
        });

        if (product != null) {
            product.removeFromInventory(difference, note);
            productRepository.save(product);
        }
    }

    @Override
    public BaseResponse updateImage(MultipartFile file, long imageId) {
        try {
            ImageEntity imageEntity = imageRepository.findById(imageId).orElseThrow(() -> {
                throw new EntityNotFoundException("Image", "Id", String.valueOf(imageId));
            });
            if (!imageTypes.contains(file.getContentType())) {
                throw new BaseException("Tệp không được phép");
            }
            imageEntity.setData(file.getBytes());
            imageRepository.save(imageEntity);
            return BaseResponse.builder()
                    .data(ImageResponse.builder()
                            .id(imageEntity.getId())
                            .url(imageEntity.getUrl())
                            .build())
                    .statusCode(HttpStatus.CREATED.value())
                    .message("Cập nhật hình ảnh thành công.")
                    .build();
        } catch (IOException e) {
            throw new BaseException("Cập nhật hình ảnh không thành công.");
        }
    }

    public ProductResponse entityToResponse(ProductEntity product, AccountCreateAndUpdate accountResponseUpdate,
            AccountCreateAndUpdate accountResponseCreate) {
        return ProductResponse.builder()
                .id(product.getId())
                .code(product.getCode())
                .name(product.getName())
                .priceIn(product.getPriceIn())
                .quantityWarning(product.getQuantityWarning())
                .priceOut(product.getPriceOut())
                .brand(product.getBrand())
                .status(product.getStatus())
                .description(product.getDescription())
                .storageQuantity(product.getStorageQuantity())
                .quantityWarning(product.getQuantityWarning())
                .imageUrl(product.getImageUrl())
                .unit(product.getUnit())
                .isActive(product.getIsActive())
                .createAt(product.getCreateDate())
                .modifyBy(accountResponseUpdate)
                .modifyAt(product.getModifyDate())
                .createBy(accountResponseCreate)
                .build();
    }

    private String getImage(ProductEntity product) {
        return ServletUriComponentsBuilder
                .fromCurrentContextPath()
                .path("/api/products/images/")
                .path(product.getId().toString())
                .toUriString();
    }

    private AccountCreateAndUpdate changeTypeAccount(Long id) {
        Object[] accountFind = accountRepository.findAccountById(id).get(0);
        return AccountCreateAndUpdate.builder()
                .id((Long) accountFind[0])
                .name((String) accountFind[1])
                .build();
    }

    @Override
    public BaseResponse getByParams(Map<String, String> params) {

        ProductParams productParams = new ProductParams(params);
        String fixedSortBy = CaseFormat.LOWER_UNDERSCORE.to(CaseFormat.LOWER_CAMEL, productParams.getSortBy());

        Sort sort = Sort.by(
                productParams.getSortOrder(),
                fixedSortBy);

        Pageable pageable = PageRequest.of(
                productParams.getPageNumber() - 1,
                productParams.getPageSize(),
                sort);

        Page<ProductEntity> products = productRepository.getAllByParams(productParams, pageable);

        return BaseResponse.builder()
                .statusCode(HttpStatus.OK.value())
                .message("Nhận sản phẩm thành công.")
                .data(new PageImpl<>(products.stream().map(productEntity -> {
                    return this.entityToResponse(productEntity, null, null);
                }).collect(Collectors.toList()), pageable, products.getTotalElements()))
                .build();
    }
}
