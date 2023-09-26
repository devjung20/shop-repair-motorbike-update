package com.example.demo.repository;

import com.example.demo.entity.EmployeeEntity;
import com.example.demo.model.params.StatisticEmployee;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface EmployeeRepository extends BaseRepository<EmployeeEntity> {
        Boolean existsByEmailAndIsActive(String email, Boolean isActive);

        @Query(value = "select e.id , e.code , e.name , e.phone , e.type , e.salary , e.is_active " +
                        "from employees as e", nativeQuery = true)
        Page<Object[]> getAllEmployee(Pageable pageable);

        @Query(value = "select id from employees where email = ?1 and is_active = true", nativeQuery = true)
        Optional<Long> getIdByEmail(String email);

        @Query(value = "select * " +
                        "from employees " +
                        "where id = ?1  and is_active = true", nativeQuery = true)
        Optional<EmployeeEntity> getRepairerIsActive(Long id);

        @Query(value = "SELECT * from employees where id = ?1 and is_active = true", nativeQuery = true)
        Optional<EmployeeEntity> findByIdAndIsActive(Long id);

        @Query(value = "select e.id , e.code , e.name , e.phone , e.type , e.salary , e.is_active " +
                        "from employees as e where is_active = true", nativeQuery = true)
        Page<Object[]> getAllEmployeeActive(Pageable pageable);

        @Query(value = "SELECT * FROM employees e " +
                        "WHERE (:name IS NULL OR e.name LIKE %:name%) " +
                        "AND (:code IS NULL OR e.code LIKE %:code%) " +
                        "AND (:phone IS NULL OR e.phone LIKE %:phone%) " +
                        "AND (:email IS NULL OR e.email LIKE %:email%) " +
                        "AND (:type IS NULL OR e.type = :type) " +
                        "AND (:status IS NULL OR e.status = :status) " +
//                        "AND (:isWorking IS NULL OR e.is_working = :isWorking) " +
                        "AND (e.is_active = true)", nativeQuery = true)
        Page<EmployeeEntity> getAllByParams(
                        @Param("name") String name,
                        @Param("code") String code,
                        @Param("phone") String phone,
                        @Param("email") String email,
                        @Param("type") String type,
                        @Param("status") Boolean status,
//                        @Param("isWorking") Boolean isWorking,
                        Pageable pageable);

        @Query(value = "select e.id , e.name ,e.code, e.type " +
                        "from employees as e " +
                        "left join accounts as a on e.id = a.employee_id " +
                        "where (e.type = 'DISPATCHER' or e.type = 'MANAGER') and a.id is null;", nativeQuery = true)
        List<Object[]> getAllByNoAccount();

    @Query(value = "select  e.id , e.code , e.name , e.salary , e.type ," +
            "case " +
            "   when e.type = 'DISPATCHER' then sum(case when o.id is null then 0 else o_s.salary_dispatcher*o_s.quantity end) " +
            "   when e.type = 'REPAIRER' then sum(case when o.id is null then 0 else o_s.salary_repairer*o_s.quantity end) " +
            "   else 0 " +
            "end as income " +
            "from employees as e " +
            "left join orders as o on o.type = 'INVOICE'  and (e.id = o.repairer_id or e.id = o.dispatcher_id) and (" +
            "(:#{#statisticEmployee.startTime} is null or " +
            ":#{#statisticEmployee.endTime} is null or " +
            "o.create_date between :#{#statisticEmployee.startTime} and :#{#statisticEmployee.endTime}) " +
            ")" +
            "left join order_service as o_s on o_s.order_id = o.id " +
            "where " +
            "(:#{#statisticEmployee.code} is null or UPPER(e.code) like UPPER(concat('%',:#{#statisticEmployee.code},'%'))) and " +
            "(:#{#statisticEmployee.name} is null or upper(e.name) like UPPER(concat('%',:#{#statisticEmployee.name},'%'))) and " +
            "(:#{#statisticEmployee.type} is null or e.type like concat('%',:#{#statisticEmployee.type},'%')) " +
            "group by e.id;"
            ,nativeQuery = true)
    Page<Object[]> statisticSalaryEmployee(@Param("statisticEmployee")StatisticEmployee statisticEmployee , Pageable pageable);

    @Query("select case when count(e) > 0 then true else false end " +
            "from EmployeeEntity e " +
            "where e.phone = :phone and e.isActive = true")
    boolean existsByPhoneAndIsActive(@Param("phone") String phone);

    @Query("select case when count(e) > 0 then true else false end " +
            "from EmployeeEntity e " +
            "where e.email = :email and e.isActive = true")
    boolean existsByEmailAndIsActive(@Param("email") String email);

}
