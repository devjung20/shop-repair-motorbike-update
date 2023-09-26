package com.example.demo.entity;

import com.example.demo.constant.CodeConstant;
import jakarta.persistence.Entity;
import jakarta.persistence.PostPersist;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "customers")
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class CustomerEntity extends BaseEntity {
    private String code;
    private String name;
    private String phone;
    private String address;
    private String email;

    @PostPersist
    public void postPersist() {
        String idString = String.format("%06d", this.getId());
        code = CodeConstant.CODE_CUSTOMER + idString;
    }
}
