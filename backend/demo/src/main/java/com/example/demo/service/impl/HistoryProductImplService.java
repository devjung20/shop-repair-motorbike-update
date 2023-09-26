package com.example.demo.service.impl;

import com.example.demo.entity.HistoryEntity;
import com.example.demo.entity.ProductEntity;
import com.example.demo.entity.enums.Action;
import com.example.demo.exception.BaseException;
import com.example.demo.exception.EntityNotFoundException;
import com.example.demo.model.BaseResponse;
import com.example.demo.model.params.HistoryParams;
import com.example.demo.model.request.HistoryProductRequest;
import com.example.demo.model.response.HistoryProductResponse;
import com.example.demo.repository.HistoryProductRepository;
import com.example.demo.repository.ProductRepository;
import com.example.demo.service.HistoryProductService;
import org.springframework.data.domain.*;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class HistoryProductImplService implements HistoryProductService {
    private final HistoryProductRepository historyProductRepository;
    private final ProductRepository productRepository;

    public HistoryProductImplService(HistoryProductRepository historyProductRepository,
            ProductRepository productRepository) {
        this.historyProductRepository = historyProductRepository;
        this.productRepository = productRepository;
    }

    @Override
    public BaseResponse getAllHistoryProduct(int pageNumber, int pageSize) {
        try {
            Pageable pageable = Pageable.ofSize(pageSize).withPage(pageNumber);
            Page<HistoryEntity> historyPage = historyProductRepository.findAll(pageable);
            List<HistoryProductResponse> historyProductResponses = historyPage.getContent().stream().map(history -> {
                return this.entityToResponse(history);
            }).collect(Collectors.toList());

            Page<HistoryProductResponse> historyProductResponsePage = new PageImpl<>(
                    historyProductResponses, pageable, historyPage.getTotalElements());

            return BaseResponse.builder()
                    .statusCode(HttpStatus.OK.value())
                    .message("Danh sách lịch sử xuất nhập kho")
                    .data(historyProductResponsePage)
                    .build();
        } catch (Exception e) {
            return BaseResponse.builder()
                    .statusCode(HttpStatus.INTERNAL_SERVER_ERROR.value())
                    .message("Lỗi xuất danh sách lịch sử xuất nhập kho")
                    .data(null)
                    .build();
        }
    }

    @Override
    public BaseResponse searchByNameAndAction(int pageNumber, int pageSize, String productName, String action,
            String orderBy) {
        try {
            Pageable pageable = PageRequest.of(pageNumber, pageSize);
            Page<HistoryEntity> historyPage = historyProductRepository.findByNameAndAction(productName, action, orderBy,
                    pageable);

            List<HistoryProductResponse> historyProductResponses = historyPage.getContent().stream().map(history -> {
                return this.entityToResponse(history);
            }).collect(Collectors.toList());

            Page<HistoryProductResponse> historyProductResponsePage = new PageImpl<>(
                    historyProductResponses, pageable, historyPage.getTotalElements());

            return BaseResponse.builder()
                    .statusCode(HttpStatus.OK.value())
                    .message("Tìm kiếm và lọc lịch sử thành công!")
                    .data(historyProductResponsePage)
                    .build();
        } catch (Exception e) {
            return BaseResponse.builder()
                    .statusCode(HttpStatus.INTERNAL_SERVER_ERROR.value())
                    .message("Lỗi khi tìm kiếm và lọc lịch sử!")
                    .data(null)
                    .build();
        }
    }

    @Override
    @Transactional
    public BaseResponse createHistory(HistoryProductRequest historyProductRequest, Boolean check, Boolean checkImOrEx) {
        historyProductRequest.getHistoryDetails().stream().forEach(historyDetail -> {
            ProductEntity product = productRepository.findById(historyDetail.getProductId())
                    .orElseThrow(() -> {
                        throw new EntityNotFoundException("Product", "Id",
                                String.valueOf(historyDetail.getProductId()));
                    });
            Boolean checkImportOrExport = checkImOrEx;
            if (check) {
                if (historyDetail.getDifference() >= 0) {
                    checkImportOrExport = true;
                } else {
                    String productName = "";

                    Optional<ProductEntity> productEntity = productRepository.findById(historyDetail.getProductId());
                    if (productEntity.isPresent()) {
                        productName = productEntity.get().getName();
                    }

                    if (productRepository.checkExistByIdAndQuantity(historyDetail.getProductId(),
                            -historyDetail.getDifference()) == 0) {
                        throw new BaseException(
                                "Kho không có đủ linh kiện: " + productName);
                    }
                    checkImportOrExport = false;
                }
            }
            HistoryEntity history = HistoryEntity.builder()
                    .product(product)
                    .priceOut(product.getPriceOut())
                    .priceIn(product.getPriceIn())
                    .productName(product.getName())
                    .unit(product.getUnit())
                    .action(checkImportOrExport ? Action.IMPORT : Action.EXPORT)
                    .quantityLeft(!check ? product.getStorageQuantity()
                            : product.getStorageQuantity() + historyDetail.getDifference())
                    .note(historyProductRequest.getNote())
                    .difference(historyDetail.getDifference())
                    .build();
            historyProductRepository.save(history);
            if (check) {
                product.setStorageQuantity(product.getStorageQuantity() + historyDetail.getDifference());
                productRepository.save(product);
            }
        });
        return BaseResponse.builder()
                .data(null)
                .statusCode(HttpStatus.CREATED.value())
                .message("Lịch sử hành động thành công.")
                .build();
    }

    @Override
    public BaseResponse getHistoryProductByConditons(Map<String, String> map) {
        HistoryParams historyParams = new HistoryParams(map);
        Pageable pageable = PageRequest.of(historyParams.getPageNumber() - 1, historyParams.getPageSize(), Sort.by(
                historyParams.getSortOrder(),
                historyParams.getSortBy()));
        Page<HistoryEntity> historyEntities = this.historyProductRepository.getByConditions(historyParams, pageable);
        List<HistoryProductResponse> historyProductResponses = historyEntities.getContent().stream()
                .map(historyEntity -> {
                    return this.entityToResponse(historyEntity);
                }).collect(Collectors.toList());
        return BaseResponse.builder()
                .message("thành công.")
                .statusCode(HttpStatus.OK.value())
                .data(new PageImpl<>(historyProductResponses, pageable, historyEntities.getTotalElements()))
                .build();
    }

    private HistoryProductResponse entityToResponse(HistoryEntity history) {
        return HistoryProductResponse.builder()
                .id(history.getId())
                .productName(history.getProductName())
                .priceIn(history.getPriceIn())
                .priceOut(history.getPriceOut())
                .difference(history.getDifference())
                .quantityLeft(history.getQuantityLeft())
                .unit(history.getUnit())
                .note(history.getNote())
                .action(history.getAction().toString())
                .createDate(history.getCreateDate())
                .build();
    }
}
