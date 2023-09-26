package com.example.demo.repository;

import com.example.demo.entity.OrderEntity;
import com.example.demo.model.params.OrderParams;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface OrderRepository extends BaseRepository<OrderEntity> {
        @Query(value = "select * " +
                        "from orders " +
                        "where id = ?1 and type = ?2 and is_active = true", nativeQuery = true)
        Optional<OrderEntity> getByIdAndType(Long id, String type);

        @Query(value = "select o.id, o.code , c.code , c.name , e.name , e1.name , o.motorbike_code , o.create_date  , o.type "
                        +
                        "from " +
                        "orders as o " +
                        "left join employees as e on e.id = o.dispatcher_id " +
                        "left join employees as e1 on e1.id = o.repairer_id " +
                        "left join customers as c on c.id = o.customer_id " +
                        "where o.is_active = true and " +
                        "(:#{#orderParams.code} is null or UPPER(o.code) like concat('%',UPPER(:#{#orderParams.code}),'%')) and "
                        +
                        "(:#{#orderParams.customerCode} is null or UPPER(c.code) like concat('%',UPPER(:#{#orderParams.customerCode}),'%')) and "
                        +
                        "(:#{#orderParams.customerName} is null or UPPER(c.name) like concat('%',UPPER(:#{#orderParams.customerName}),'%')) and "
                        +
                        "(:#{#orderParams.dispatcherName} is null or UPPER(e.name) like concat('%',UPPER(:#{#orderParams.dispatcherName}),'%')) and "
                        +
                        "(:#{#orderParams.repairerName} is null or UPPER(e1.name) like concat('%',UPPER(:#{#orderParams.repairerName}),'%')) and "
                        +
                        "(:#{#orderParams.motorbikeCode} is null or UPPER(o.motorbike_code) like concat('%',UPPER(:#{#orderParams.motorbikeCode}),'%')) and "
                        +
                        "(:#{#orderParams.type} is null or UPPER(o.type) like concat('%',UPPER(:#{#orderParams.type}),'%')) and "
                        +
                        "(:#{#orderParams.startTime} is null or " +
                        ":#{#orderParams.endTime} is null or " +
                        "o.create_date between :#{#orderParams.startTime} and :#{#orderParams.endTime})", nativeQuery = true)
        Page<Object[]> getOrderByConditions(@Param("orderParams") OrderParams orderParams, Pageable pageable);

        @Query(value = "select * " +
                        "from orders " +
                        "where id = ?1 and type = 'ORDER'", nativeQuery = true)
        Optional<OrderEntity> findByIdAndTypeIsOrder(Long orderId);
}
