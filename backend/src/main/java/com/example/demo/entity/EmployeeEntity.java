package com.example.demo.entity;

import com.example.demo.constant.CodeConstant;
import com.example.demo.entity.enums.Gender;
import com.example.demo.entity.enums.TypeEmployee;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Date;

@Entity
@Table(name = "employees")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EmployeeEntity extends BaseEntity {

    private String code;
    private String name;
    private Date birthday;
    private String phone;
    private String email;
    private Double salary;
    private String address;
    private Boolean status;

//    private Boolean isWorking;
    @Enumerated(EnumType.STRING)
    private Gender gender;

    @Enumerated(EnumType.STRING)
    private TypeEmployee type;

    @PostPersist
    public void postPersist() {
        String idString = String.format("%06d", this.getId());
        code = CodeConstant.CODE_EMPLOYEE + idString;
    }
}
