package com.example.demo.repository;

import com.example.demo.entity.ImageEntity;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface ImageRepository extends BaseRepository<ImageEntity> {
  @Transactional
  void deleteByProductIdIsNull();

  @Transactional
  @Modifying
  @Query(value = "delete from images where id != ?2 and product_id = ?1", nativeQuery = true)
  void deleteAllByProductId(Long id, Long imageId);
}
