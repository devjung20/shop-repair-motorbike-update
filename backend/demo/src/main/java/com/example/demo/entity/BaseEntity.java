package com.example.demo.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@MappedSuperclass
@Data
public abstract class BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createDate;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime modifyDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "create_by")
    private AccountEntity createBy;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "modify_by")
    private AccountEntity modifyBy;
    private Boolean isActive;

    @PrePersist
    public void prePersist() {
        createDate = LocalDateTime.now();
        modifyDate = LocalDateTime.now();
    }

    @PreUpdate
    public void preUpdate() {
        modifyDate = LocalDateTime.now();
    }
}
