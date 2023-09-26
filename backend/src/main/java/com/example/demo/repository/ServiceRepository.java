package com.example.demo.repository;

import com.example.demo.entity.ServiceEntity;
import com.example.demo.model.response.BaseService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ServiceRepository extends BaseRepository<ServiceEntity> {
    @Query(value = "select s.id , s.name , s.price , s.code " +
            "from services s where s.is_active = true", nativeQuery = true)
    Page<Object[]> getAll(Pageable pageable);

    boolean existsByIdAndIsActive(Long id, boolean isActive);

    @Query(value = "select s.id , s.name , s.price , s.code, s.description "
            +
            "from services s where " +
            "(:#{#service.name} is null or lower(s.name) like concat('%',lower(:#{#service.name}),'%')) and "
            +
            "(:#{#service.code} is null or lower(s.code) like concat('%',lower(:#{#service.code}),'%')) and "
            +
            "(:#{#service.price} is null or s.price = :#{#service.price}) and is_active = true", nativeQuery = true)
    Page<Object[]> getByParamsInService(@Param("service") BaseService baseService, Pageable pageable);
}
