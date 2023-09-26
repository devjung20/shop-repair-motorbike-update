package com.example.demo.repository;

import com.example.demo.entity.ForgotPasswordEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ForgotPasswordRepository extends JpaRepository<ForgotPasswordEntity , Long> {
    @Query(value = "select * from tbl_forgot_password where account_create = ?1",nativeQuery = true)
    List<ForgotPasswordEntity> getAllByAccountCreate(Long accountId);

    @Query(value = "select * from tbl_forgot_password where code = ?1", nativeQuery = true)
    Optional<ForgotPasswordEntity> getByCode(String code);

    @Query(value = "select * from tbl_forgot_password where timestampdiff(second , create_at , now()) > 300" , nativeQuery = true)
    List<ForgotPasswordEntity> getListByExpiredTime();
}
