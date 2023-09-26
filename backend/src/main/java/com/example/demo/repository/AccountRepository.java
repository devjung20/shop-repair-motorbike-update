package com.example.demo.repository;

import com.example.demo.entity.AccountEntity;
import com.example.demo.entity.EmployeeEntity;
import com.example.demo.model.response.BaseAccount;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AccountRepository extends BaseRepository<AccountEntity> {

    Optional<AccountEntity> getAccountEntityByEmailAndIsActive(String email , Boolean isActive);

    @Query(value = "select case when count(a) > 0 then true else false end " +
            "from AccountEntity a " +
            "where a.email = :email and a.isActive = true")
    Boolean existsByEmailAndIsActive(@Param("email") String email);

    @Query(value = "select * from accounts where id = ?1 and is_active = true", nativeQuery = true)
    Optional<AccountEntity> getAccountById(Long id);

    @Query(value = "select a.id as id , a.user_name as username " +
            "from accounts a " +
            "where a.id = ?1 and a.is_active = true", nativeQuery = true)
    List<Object[]> findAccountById(Long id);

//    Boolean existsByEmployeeAndIsActive(EmployeeEntity employee,Boolean isActive);

    @Query(value = "SELECT * FROM accounts a " +
            "WHERE " +
            "(:#{#account.email} IS NULL OR LOWER(a.email) LIKE CONCAT('%', LOWER(:#{#account.email}), '%')) AND " +
            "(:#{#account.userName} IS NULL OR LOWER(a.user_name) LIKE CONCAT('%', LOWER(:#{#account.userName}), '%')) AND " +
            "(:#{#account.type} IS NULL OR LOWER(a.role) LIKE CONCAT('%', LOWER(:#{#account.type}), '%')) AND " +
            "(a.is_active = TRUE)", nativeQuery = true)
    Page<AccountEntity> searchByManyConditions(@Param("account") BaseAccount account, Pageable pageable);

    Optional<AccountEntity> getByEmailAndIsActive(String email, Boolean isActive);
}
