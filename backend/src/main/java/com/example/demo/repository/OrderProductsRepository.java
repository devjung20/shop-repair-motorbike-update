package com.example.demo.repository;

import com.example.demo.entity.OrderProduct;
import com.example.demo.model.params.TimeParams;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderProductsRepository extends BaseRepository<OrderProduct> {

        @Query(value = "select * " +
                        "from order_product " +
                        "where order_id = ?1", nativeQuery = true)

        List<OrderProduct> getAllByOrderId(Long id);

        @Query(value = "select p.id, p.name, p.price_out as price, p.unit ,sum(o_p.quantity) as sum " +
                        "from order_product as o_p " +
                        "inner join products as p on p.id = o_p.product_id " +
                        "where " +
                        "(:#{#time.startTime} is null or :#{#time.endTime} is null " +
                        "or o_p.create_date between :#{#time.startTime} and :#{#time.endTime}) " +
                        "group by p.id " +
                        "order by sum desc;", nativeQuery = true)
        List<Object[]> statisticTopProduct(@Param("time") TimeParams timeParams);
}
