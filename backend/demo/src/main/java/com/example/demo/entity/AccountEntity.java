package com.example.demo.entity;

import com.example.demo.entity.enums.Role;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "accounts")
@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class AccountEntity extends BaseEntity {
    private String userName;
    private String email;
    private String password;

    @Enumerated(EnumType.STRING)
    private Role role;

}
