package com.example.demo.entity;

import com.example.demo.constant.CodeConstant;
import com.example.demo.entity.enums.Action;
import com.example.demo.exception.EntityNotFoundException;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "history_product")
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class HistoryEntity extends BaseEntity {
    private int difference;
    private String note;
    private int quantityLeft;

    @Enumerated(EnumType.STRING)
    private Action action;

    private String code;
    private String productName;
    private Double priceIn;
    private Double priceOut;
    private String unit;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id")
    private ProductEntity product;

    public String getProductName() {
        if (product != null && product.getName() != null) {
            return product.getName();
        }
        throw new EntityNotFoundException("ProductEntity", "name", product.getName().toString());
    }

    @PostPersist
    public void postPersist() {
        String idString = String.format("%06d", this.getId());
        code = CodeConstant.HISTORY_CODE + idString;
    }

    public Double getPriceIn() {
        if (product != null && product.getPriceIn() != null) {
            return product.getPriceIn();
        }
        throw new EntityNotFoundException("ProductEntity", "name", product.getPriceIn().toString());
    }

    public Double getPriceOut() {
        if (product != null && product.getPriceOut() != null) {
            return product.getPriceOut();
        }
        throw new EntityNotFoundException("ProductEntity", "name", product.getPriceOut().toString());
    }
}
