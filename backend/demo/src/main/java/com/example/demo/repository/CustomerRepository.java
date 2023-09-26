package com.example.demo.repository;

import com.example.demo.entity.CustomerEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CustomerRepository extends BaseRepository<CustomerEntity> {
        @Query("SELECT c FROM CustomerEntity c WHERE c.isActive=true AND " +
                        "(lower(c.name) LIKE lower(concat('%', :name, '%')) OR :name IS NULL) AND " +
                        "(lower(c.phone) LIKE lower(concat('%', :phone, '%')) OR :phone IS NULL) AND " +
                        "(lower(c.email) LIKE lower(concat('%', :email, '%')) OR :email IS NULL)" +
                        "ORDER BY " +
                        "CASE WHEN :orderBy = 'nameAsc' THEN c.name END ASC," +
                        "CASE WHEN :orderBy = 'nameAsc' THEN c.name END DESC ")
        Page<CustomerEntity> findByNameAndPhoneAndEmail(
                        @Param("name") String name,
                        @Param("phone") String phone,
                        @Param("email") String email,
                        @Param("orderBy") String orderBy,
                        Pageable pageable);

        @Query("SELECT c FROM CustomerEntity c WHERE c.isActive=true AND " +
                        "(:code IS NULL OR c.code like %:code%) AND " +
                        "(:name IS NULL OR c.name like %:name%) AND " +
                        "(:phone IS NULL OR c.phone like %:phone%) AND " +
                        "(:email IS NULL OR c.email like %:email%)")
        Page<CustomerEntity> getAllByParams(
                        @Param("code") String code,
                        @Param("name") String name,
                        @Param("phone") String phone,
                        @Param("email") String email,
                        Pageable pageable);

        @Query(value = "select c.id, c.code, c.name, c.phone, c.address, c.email, " +
                        "c.is_active, c.create_by, c.create_date, c.modify_by,c.modify_date " +
                        "from customers as c where is_active = true", nativeQuery = true)
        Page<CustomerEntity> getAllCustomersActive(Pageable pageable);

        @Query(value = "SELECT COUNT(*) FROM customers WHERE phone = :phone AND phone IS NOT NULL AND is_active = true", nativeQuery = true)
        int countByPhone(String phone);

        @Query(value = "SELECT COUNT(*) FROM customers WHERE email = :email AND email IS NOT NULL AND is_active = true", nativeQuery = true)
        int countByEmail(String email);

        Optional<CustomerEntity> findByIdAndIsActive(Long id, Boolean isActive);
}
