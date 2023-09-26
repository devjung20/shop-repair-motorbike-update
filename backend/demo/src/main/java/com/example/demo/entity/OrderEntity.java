package com.example.demo.entity;

import com.example.demo.constant.CodeConstant;
import com.example.demo.entity.enums.TypeOrder;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "orders")
@Builder
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class OrderEntity extends BaseEntity {
    private String code;

    @Enumerated(EnumType.STRING)
    private TypeOrder type;

    private String note;
    private String motorbikeCode;
    private String motorbikeName;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.MERGE)
    @JoinColumn(name = "dispatcher_id")
    private EmployeeEntity dispatcher;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.MERGE)
    @JoinColumn(name = "repairer_id")
    private EmployeeEntity repairer;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "customer_id")
    private CustomerEntity customerEntity;

    @PostPersist
    public void postPersist() {
        String idString = String.format("%06d", this.getId());
        code = CodeConstant.CODE_ORDER + idString;
    }

    public EmployeeEntity getDispatcher() {
        return dispatcher;
    }

    public EmployeeEntity getRepairer() {
        return repairer;
    }

    public CustomerEntity getCustomerEntity() {
        return customerEntity;
    }

    public String getCode() {
        return code;
    }

    public TypeOrder getType() {
        return type;
    }

    public String getNote() {
        return note;
    }

    public String getMotorbikeCode() {
        return motorbikeCode;
    }

    public String getMotorbikeName() {
        return motorbikeName;
    }
}
