package com.example.demo.repository;

import com.example.demo.entity.HistoryEntity;
import com.example.demo.model.params.HistoryParams;
import com.example.demo.model.params.StatisticProductParams;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface HistoryProductRepository extends BaseRepository<HistoryEntity> {
        @Query("SELECT h FROM HistoryEntity h WHERE " +
                        "(lower(h.productName) like lower(concat('%',:productName,'%')) OR :productName IS NULL) AND " +
                        "(lower(h.action) like lower(concat('%',:action,'%')) OR :action IS NULL) " +
                        "ORDER BY " +
                        "CASE WHEN :orderBy = 'productNameAsc' THEN  h.productName END ASC," +
                        "CASE WHEN :orderBy = 'productNameDesc' THEN  h.productName END DESC ")
        Page<HistoryEntity> findByNameAndAction(
                        @Param("productName") String productName,
                        @Param("action") String action,
                        @Param("orderBy") String orderBy,
                        Pageable pageable);

        @Query(value = "select * from history_product where "
                        + "(:#{#history.productName} is null or lower(history_product.product_name) like concat('%',lower(:#{#history.productName}),'%')) and "
                        + "(:#{#history.note} is null or lower(history_product.note) like concat('%',lower(:#{#history.note}),'%')) and "
                        + "(:#{#history.action} is null or history_product.action like concat('%',:#{#history.action},'%')) and "
                        + "(:#{#history.unit} is null or history_product.unit like concat('%',:#{#history.unit},'%')) and "
                        + "(:#{#history.priceIn} is null or history_product.price_in = :#{#history.priceIn}) and "
                        + "(:#{#history.priceOut} is null or history_product.price_out = :#{#history.priceOut}) and "
                        + "(:#{#history.startTime} is null or :#{#history.endTime} is null or history_product.create_date between :#{#history.startTime} and :#{#history.endTime})", nativeQuery = true)
        Page<HistoryEntity> getByConditions(@Param("history") HistoryParams historyParams, Pageable pageable);

        @Query(value = "select p.id , p.code, p.name , p.price_in , p.price_out , " +
                        "CAST(-1 * SUM(CASE WHEN h_p.difference < 0 THEN h_p.difference ELSE 0 END) AS SIGNED) AS ex, "
                        +
                        "CAST(SUM(CASE WHEN h_p.difference >= 0 THEN h_p.difference ELSE 0 END) AS SIGNED) AS im ," +
                        "-1*sum( " +
                        "case " +
                        " when h_p.difference >= 0 then h_p.difference*p.price_in " +
                        "        else h_p.difference*p.price_out " +
                        "end " +
                        ") as income " +
                        "from history_product as h_p " +
                        "inner join products as p on h_p.product_id = p.id " +
                        "group by p.id " +
                        ") " +
                        "" +
                        "select c.* , (c.price_out*ex - c.price_in*im) as income " +
                        "from Caculation as c " +
                        "order by c.id; ", nativeQuery = true)
        List<Object[]> statisticHistoryProduct();

        @Query(value = "WITH CalculationIn AS (" +
                        "SELECT h_p.product_id as id, p.code, h_p.product_name as name, h_p.unit, h_p.price_in AS price,"
                        +
                        "       SUM(h_p.difference) AS total_quantity_in," +
                        "       SUM(-h_p.price_in * h_p.difference) AS total_income_in " +
                        "FROM history_product AS h_p " +
                        "INNER JOIN products AS p ON h_p.product_id = p.id " +
                        "WHERE h_p.difference > 0 " +
                        "AND (:#{#params.startTime} is null or :#{#params.endTime} is null or " +
                        "h_p.create_date between :#{#params.startTime} and :#{#params.endTime}) " +
                        "GROUP BY h_p.product_id, p.code, h_p.product_name, h_p.unit, h_p.price_in " +
                        ") " +
                        "SELECT c_in.id, c_in.code, c_in.name, c_in.unit, c_in.price," +
                        "       c_in.total_quantity_in AS total_quantity," +
                        "       c_in.total_income_in AS total_income " +
                        "FROM CalculationIn AS c_in " +
                        "ORDER BY c_in.id", nativeQuery = true)
        Page<Object[]> statisticHistoryProductIn(
                        @Param("params") StatisticProductParams statisticProductParams, Pageable pageable);

        @Query(value = "WITH CalculationOut AS (" +
                        "SELECT h_p.product_id as id, p.code, h_p.product_name as name, h_p.unit, h_p.price_out AS price,"
                        +
                        "       SUM(-h_p.difference) AS total_quantity_out," +
                        "       SUM(-h_p.price_out * h_p.difference) AS total_income_out " +
                        "FROM history_product AS h_p " +
                        "INNER JOIN products AS p ON h_p.product_id = p.id " +
                        "WHERE h_p.difference < 0 " +
                        "AND (:#{#params.startTime} is null or :#{#params.endTime} is null or " +
                        "h_p.create_date between :#{#params.startTime} and :#{#params.endTime}) " +
                        "GROUP BY h_p.product_id, p.code, h_p.product_name, h_p.unit, h_p.price_out " +
                        ") " +
                        "SELECT c_out.id, c_out.code, c_out.name, c_out.unit, c_out.price," +
                        "       c_out.total_quantity_out AS total_quantity," +
                        "       c_out.total_income_out AS total_income " +
                        "FROM CalculationOut AS c_out " +
                        "ORDER BY c_out.id", nativeQuery = true)
        Page<Object[]> statisticHistoryProductOut(
                        @Param("params") StatisticProductParams statisticProductParams, Pageable pageable);

        @Query(value = "WITH CalculationOutIn AS (" +
                        "SELECT p.id as id, p.code, h_p.product_name as name, h_p.unit, h_p.price_in, h_p.price_out, "
                        +
                        "       SUM(h_p.difference) AS total_quantity_out, " +
                        "       SUM(h_p.price_in * h_p.difference - h_p.price_out * h_p.difference) AS total_income_out "
                        +
                        "FROM history_product AS h_p " +
                        "INNER JOIN products AS p ON h_p.product_id = p.id " +
                        "WHERE h_p.difference < 0 " +
                        "AND (:#{#params.startTime} is null or :#{#params.endTime} is null or " +
                        "h_p.create_date between :#{#params.startTime} and :#{#params.endTime}) " +
                        "GROUP BY p.id, p.code, h_p.product_name, h_p.unit, h_p.price_in, h_p.price_out " +
                        ") " +
                        "SELECT c_out.id, c_out.code, c_out.name, c_out.unit, c_out.price_in, c_out.price_out, " +
                        "       c_out.total_quantity_out AS total_quantity," +
                        "       c_out.total_income_out AS total_income " +
                        "FROM CalculationOutIn AS c_out " +
                        "ORDER BY c_out.id", nativeQuery = true)
        Page<Object[]> statisticHistoryProductOutIn(
                        @Param("params") StatisticProductParams statisticProductParams, Pageable pageable);

        @Query(value = "SELECT substring(dates.dt, -2) as month, substring(dates.dt, 1, 4) as year, sum(hp.difference * hp.price_in) as expense "
                        +
                        "FROM (" +
                        "  SELECT DATE_FORMAT(CURDATE(), '%Y-%m') AS dt UNION ALL " +
                        "  SELECT DATE_FORMAT(DATE_SUB(CURDATE(), INTERVAL 1 MONTH), '%Y-%m') UNION ALL " +
                        "  SELECT DATE_FORMAT(DATE_SUB(CURDATE(), INTERVAL 2 MONTH), '%Y-%m') UNION ALL " +
                        "  SELECT DATE_FORMAT(DATE_SUB(CURDATE(), INTERVAL 3 MONTH), '%Y-%m') UNION ALL " +
                        "  SELECT DATE_FORMAT(DATE_SUB(CURDATE(), INTERVAL 4 MONTH), '%Y-%m') UNION ALL " +
                        "  SELECT DATE_FORMAT(DATE_SUB(CURDATE(), INTERVAL 5 MONTH), '%Y-%m') UNION ALL " +
                        "  SELECT DATE_FORMAT(DATE_SUB(CURDATE(), INTERVAL 6 MONTH), '%Y-%m') UNION ALL " +
                        "  SELECT DATE_FORMAT(DATE_SUB(CURDATE(), INTERVAL 7 MONTH), '%Y-%m') UNION ALL " +
                        "  SELECT DATE_FORMAT(DATE_SUB(CURDATE(), INTERVAL 8 MONTH), '%Y-%m') UNION ALL " +
                        "  SELECT DATE_FORMAT(DATE_SUB(CURDATE(), INTERVAL 9 MONTH), '%Y-%m') UNION ALL " +
                        "  SELECT DATE_FORMAT(DATE_SUB(CURDATE(), INTERVAL 10 MONTH), '%Y-%m') UNION ALL " +
                        "  SELECT DATE_FORMAT(DATE_SUB(CURDATE(), INTERVAL 11 MONTH), '%Y-%m') UNION ALL " +
                        "  SELECT DATE_FORMAT(DATE_SUB(CURDATE(), INTERVAL 12 MONTH), '%Y-%m') UNION ALL " +
                        "  SELECT DATE_FORMAT(DATE_SUB(CURDATE(), INTERVAL 13 MONTH), '%Y-%m') UNION ALL " +
                        "  SELECT DATE_FORMAT(DATE_SUB(CURDATE(), INTERVAL 14 MONTH), '%Y-%m') UNION ALL " +
                        "  SELECT DATE_FORMAT(DATE_SUB(CURDATE(), INTERVAL 15 MONTH), '%Y-%m') UNION ALL " +
                        "  SELECT DATE_FORMAT(DATE_SUB(CURDATE(), INTERVAL 16 MONTH), '%Y-%m') UNION ALL " +
                        "  SELECT DATE_FORMAT(DATE_SUB(CURDATE(), INTERVAL 17 MONTH), '%Y-%m') UNION ALL " +
                        "  SELECT DATE_FORMAT(DATE_SUB(CURDATE(), INTERVAL 18 MONTH), '%Y-%m') UNION ALL " +
                        "  SELECT DATE_FORMAT(DATE_SUB(CURDATE(), INTERVAL 19 MONTH), '%Y-%m') UNION ALL " +
                        "  SELECT DATE_FORMAT(DATE_SUB(CURDATE(), INTERVAL 20 MONTH), '%Y-%m') UNION ALL " +
                        "  SELECT DATE_FORMAT(DATE_SUB(CURDATE(), INTERVAL 21 MONTH), '%Y-%m') UNION ALL " +
                        "  SELECT DATE_FORMAT(DATE_SUB(CURDATE(), INTERVAL 22 MONTH), '%Y-%m') UNION ALL " +
                        "  SELECT DATE_FORMAT(DATE_SUB(CURDATE(), INTERVAL 23 MONTH), '%Y-%m')" +
                        ") dates LEFT JOIN history_product hp " +
                        "ON DATE_FORMAT(hp.create_date, '%m') = substring(dates.dt, -2) " +
                        "AND DATE_FORMAT(hp.create_date, '%Y') = substring(dates.dt, 1, 4) " +
                        "AND hp.difference > 0 " +
                        "GROUP BY year, month " +
                        "ORDER BY year, month", nativeQuery = true)
        List<Object[]> statisticProductInYear();

        @Query(value = "SELECT substring(dates.dt, -2) as month, substring(dates.dt, 1, 4) as year, sum(hp.difference * hp.price_out) as expense "
                        +
                        "FROM (" +
                        "  SELECT DATE_FORMAT(CURDATE(), '%Y-%m') AS dt UNION ALL " +
                        "  SELECT DATE_FORMAT(DATE_SUB(CURDATE(), INTERVAL 1 MONTH), '%Y-%m') UNION ALL " +
                        "  SELECT DATE_FORMAT(DATE_SUB(CURDATE(), INTERVAL 2 MONTH), '%Y-%m') UNION ALL " +
                        "  SELECT DATE_FORMAT(DATE_SUB(CURDATE(), INTERVAL 3 MONTH), '%Y-%m') UNION ALL " +
                        "  SELECT DATE_FORMAT(DATE_SUB(CURDATE(), INTERVAL 4 MONTH), '%Y-%m') UNION ALL " +
                        "  SELECT DATE_FORMAT(DATE_SUB(CURDATE(), INTERVAL 5 MONTH), '%Y-%m') UNION ALL " +
                        "  SELECT DATE_FORMAT(DATE_SUB(CURDATE(), INTERVAL 6 MONTH), '%Y-%m') UNION ALL " +
                        "  SELECT DATE_FORMAT(DATE_SUB(CURDATE(), INTERVAL 7 MONTH), '%Y-%m') UNION ALL " +
                        "  SELECT DATE_FORMAT(DATE_SUB(CURDATE(), INTERVAL 8 MONTH), '%Y-%m') UNION ALL " +
                        "  SELECT DATE_FORMAT(DATE_SUB(CURDATE(), INTERVAL 9 MONTH), '%Y-%m') UNION ALL " +
                        "  SELECT DATE_FORMAT(DATE_SUB(CURDATE(), INTERVAL 10 MONTH), '%Y-%m') UNION ALL " +
                        "  SELECT DATE_FORMAT(DATE_SUB(CURDATE(), INTERVAL 11 MONTH), '%Y-%m') UNION ALL " +
                        "  SELECT DATE_FORMAT(DATE_SUB(CURDATE(), INTERVAL 12 MONTH), '%Y-%m') UNION ALL " +
                        "  SELECT DATE_FORMAT(DATE_SUB(CURDATE(), INTERVAL 13 MONTH), '%Y-%m') UNION ALL " +
                        "  SELECT DATE_FORMAT(DATE_SUB(CURDATE(), INTERVAL 14 MONTH), '%Y-%m') UNION ALL " +
                        "  SELECT DATE_FORMAT(DATE_SUB(CURDATE(), INTERVAL 15 MONTH), '%Y-%m') UNION ALL " +
                        "  SELECT DATE_FORMAT(DATE_SUB(CURDATE(), INTERVAL 16 MONTH), '%Y-%m') UNION ALL " +
                        "  SELECT DATE_FORMAT(DATE_SUB(CURDATE(), INTERVAL 17 MONTH), '%Y-%m') UNION ALL " +
                        "  SELECT DATE_FORMAT(DATE_SUB(CURDATE(), INTERVAL 18 MONTH), '%Y-%m') UNION ALL " +
                        "  SELECT DATE_FORMAT(DATE_SUB(CURDATE(), INTERVAL 19 MONTH), '%Y-%m') UNION ALL " +
                        "  SELECT DATE_FORMAT(DATE_SUB(CURDATE(), INTERVAL 20 MONTH), '%Y-%m') UNION ALL " +
                        "  SELECT DATE_FORMAT(DATE_SUB(CURDATE(), INTERVAL 21 MONTH), '%Y-%m') UNION ALL " +
                        "  SELECT DATE_FORMAT(DATE_SUB(CURDATE(), INTERVAL 22 MONTH), '%Y-%m') UNION ALL " +
                        "  SELECT DATE_FORMAT(DATE_SUB(CURDATE(), INTERVAL 23 MONTH), '%Y-%m')" +
                        ") dates LEFT JOIN history_product hp " +
                        "ON DATE_FORMAT(hp.create_date, '%m') = substring(dates.dt, -2) " +
                        "AND DATE_FORMAT(hp.create_date, '%Y') = substring(dates.dt, 1, 4) " +
                        "AND hp.difference < 0 " +
                        "GROUP BY year, month " +
                        "ORDER BY year, month", nativeQuery = true)
        List<Object[]> statisticProductOutYear();

        @Query(value = "SELECT substring(dates.dt, -2) as month, substring(dates.dt, 1, 4) as year, sum(hp.difference * hp.price_in) as expense "
                        +
                        "FROM (" +
                        "  SELECT DATE_FORMAT(CURDATE(), '%Y-%m') AS dt UNION ALL " +
                        "  SELECT DATE_FORMAT(DATE_SUB(CURDATE(), INTERVAL 1 MONTH), '%Y-%m') UNION ALL " +
                        "  SELECT DATE_FORMAT(DATE_SUB(CURDATE(), INTERVAL 2 MONTH), '%Y-%m') UNION ALL " +
                        "  SELECT DATE_FORMAT(DATE_SUB(CURDATE(), INTERVAL 3 MONTH), '%Y-%m') UNION ALL " +
                        "  SELECT DATE_FORMAT(DATE_SUB(CURDATE(), INTERVAL 4 MONTH), '%Y-%m') UNION ALL " +
                        "  SELECT DATE_FORMAT(DATE_SUB(CURDATE(), INTERVAL 5 MONTH), '%Y-%m') UNION ALL " +
                        "  SELECT DATE_FORMAT(DATE_SUB(CURDATE(), INTERVAL 6 MONTH), '%Y-%m') UNION ALL " +
                        "  SELECT DATE_FORMAT(DATE_SUB(CURDATE(), INTERVAL 7 MONTH), '%Y-%m') UNION ALL " +
                        "  SELECT DATE_FORMAT(DATE_SUB(CURDATE(), INTERVAL 8 MONTH), '%Y-%m') UNION ALL " +
                        "  SELECT DATE_FORMAT(DATE_SUB(CURDATE(), INTERVAL 9 MONTH), '%Y-%m') UNION ALL " +
                        "  SELECT DATE_FORMAT(DATE_SUB(CURDATE(), INTERVAL 10 MONTH), '%Y-%m') UNION ALL " +
                        "  SELECT DATE_FORMAT(DATE_SUB(CURDATE(), INTERVAL 11 MONTH), '%Y-%m') UNION ALL " +
                        "  SELECT DATE_FORMAT(DATE_SUB(CURDATE(), INTERVAL 12 MONTH), '%Y-%m') UNION ALL " +
                        "  SELECT DATE_FORMAT(DATE_SUB(CURDATE(), INTERVAL 13 MONTH), '%Y-%m') UNION ALL " +
                        "  SELECT DATE_FORMAT(DATE_SUB(CURDATE(), INTERVAL 14 MONTH), '%Y-%m') UNION ALL " +
                        "  SELECT DATE_FORMAT(DATE_SUB(CURDATE(), INTERVAL 15 MONTH), '%Y-%m') UNION ALL " +
                        "  SELECT DATE_FORMAT(DATE_SUB(CURDATE(), INTERVAL 16 MONTH), '%Y-%m') UNION ALL " +
                        "  SELECT DATE_FORMAT(DATE_SUB(CURDATE(), INTERVAL 17 MONTH), '%Y-%m') UNION ALL " +
                        "  SELECT DATE_FORMAT(DATE_SUB(CURDATE(), INTERVAL 18 MONTH), '%Y-%m') UNION ALL " +
                        "  SELECT DATE_FORMAT(DATE_SUB(CURDATE(), INTERVAL 19 MONTH), '%Y-%m') UNION ALL " +
                        "  SELECT DATE_FORMAT(DATE_SUB(CURDATE(), INTERVAL 20 MONTH), '%Y-%m') UNION ALL " +
                        "  SELECT DATE_FORMAT(DATE_SUB(CURDATE(), INTERVAL 21 MONTH), '%Y-%m') UNION ALL " +
                        "  SELECT DATE_FORMAT(DATE_SUB(CURDATE(), INTERVAL 22 MONTH), '%Y-%m') UNION ALL " +
                        "  SELECT DATE_FORMAT(DATE_SUB(CURDATE(), INTERVAL 23 MONTH), '%Y-%m')" +
                        ") dates LEFT JOIN history_product hp " +
                        "ON DATE_FORMAT(hp.create_date, '%m') = substring(dates.dt, -2) " +
                        "AND DATE_FORMAT(hp.create_date, '%Y') = substring(dates.dt, 1, 4) " +
                        "AND hp.difference < 0 " +
                        "GROUP BY year, month " +
                        "ORDER BY year, month", nativeQuery = true)
        List<Object[]> statisticProductOutInYear();

}
