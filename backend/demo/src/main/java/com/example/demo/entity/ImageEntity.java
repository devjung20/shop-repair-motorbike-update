package com.example.demo.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Lob;
import jakarta.persistence.Table;
import lombok.*;

@Entity
@Table(name = "images")
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
public class ImageEntity extends BaseEntity {
    private String type;
    private String url;
    private Long productId;

    @Lob
    @Column(name = "data", columnDefinition = "LONGBLOB")
    private byte[] data;
}
