package com.example.demo.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "order_service")
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderServiceEntity extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id")
    private OrderEntity order;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "service_id")
    private ServiceEntity service;
    private Integer quantity;
    private String name;
    private Double price;
    private Double salaryDispatcher;
    private Double salaryRepairer;

    public Integer getQuantity() {
        return quantity;
    }

    public OrderEntity getOrder() {
        return order;
    }

    public ServiceEntity getService() {
        return service;
    }

    public String getName() {
        return name;
    }

    public Double getPrice() {
        return price;
    }

    public Double getSalaryDispatcher() {
        return salaryDispatcher;
    }

    public Double getSalaryRepairer() {
        return salaryRepairer;
    }
}
