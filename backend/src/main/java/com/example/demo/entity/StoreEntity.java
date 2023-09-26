package com.example.demo.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "stores")
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class StoreEntity extends BaseEntity{
//    private String code;
    private String phone;
    private String name;
    private String address;
    private String email;
    private Double vat;
}
