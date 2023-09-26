package com.example.demo.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "order_product")
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderProduct extends BaseEntity {
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id")
    private OrderEntity order;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id")
    private ProductEntity product;

    private Integer quantity;
    private String name;
    private Double price;
    private String unit;

    public OrderEntity getOrder() {
        return order;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public ProductEntity getProduct() {
        return product;
    }

    public String getName() {
        return name;
    }

    public Double getPrice() {
        return price;
    }

    public String getUnit() {
        return unit;
    }
}
