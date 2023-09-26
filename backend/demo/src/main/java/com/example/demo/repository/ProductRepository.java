package com.example.demo.repository;

import com.example.demo.entity.ProductEntity;

import com.example.demo.model.params.ProductParams;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepository extends BaseRepository<ProductEntity> {
        @Query(value = "select count(id) " +
                        "from products " +
                        "where id = ?1 " +
                        "and storage_quantity >= ?2 " +
                        "and is_active = true", nativeQuery = true)
        int checkExistByIdAndQuantity(Long productId, int quantity);

        @Query("SELECT p FROM ProductEntity p " +
                        "WHERE " +
                        "(:#{#productParams.status} IS NULL OR upper(p.status) LIKE concat('%', upper(:#{#productParams.status}), '%')) and "
                        +
                        "(:#{#productParams.code} IS NULL OR upper(p.code) LIKE concat('%', upper(:#{#productParams.code}), '%')) and "
                        +
                        "(:#{#productParams.name} IS NULL OR upper(p.name) LIKE concat('%', upper(:#{#productParams.name}), '%')) and "
                        +
                        "(p.isActive = TRUE)")
        Page<ProductEntity> getAllByParams(@Param("productParams") ProductParams productParams, Pageable pageable);

}
