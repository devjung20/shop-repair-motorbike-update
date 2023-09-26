package com.example.demo.entity;

import com.example.demo.constant.CodeConstant;
import jakarta.persistence.Entity;
import jakarta.persistence.PostPersist;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "services")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ServiceEntity extends BaseEntity {
    private String code;
    private String name;
    private Double price;
//    private Double salaryDispatcher;
//    private Double salaryRepairer;
    private String description;

    @PostPersist
    public void postPersist() {
        String idString = String.format("%06d", this.getId());
        code = CodeConstant.CODE_SERVICE + idString;
    }
}
