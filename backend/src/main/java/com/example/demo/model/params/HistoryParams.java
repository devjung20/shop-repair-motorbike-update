package com.example.demo.model.params;

import com.example.demo.utils.DateFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Map;

@Data
@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor
@AllArgsConstructor
public class HistoryParams extends BaseParams {
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private String productName;
    private String action;
    private String note;
    private Double priceIn;
    private Double priceOut;
    private String unit;

    public HistoryParams(Map<String, String> map) {
        super(map);
        productName = map.get("productName");
        note = map.get("note");
        unit = map.get("unit");
        priceIn = map.get("priceIn") != null ? Double.valueOf(map.get("priceIn")) : null;
        priceOut = map.get("priceOut") != null ? Double.valueOf(map.get("priceOut")) : null;
        action = map.get("action") != null ? map.get("action").toUpperCase() : null;

        LocalDate currentDate = LocalDate.now();

        startTime = LocalDateTime.of(currentDate, LocalTime.MIDNIGHT).minusYears(100);
        endTime = LocalDateTime.of(currentDate.plusDays(1), LocalTime.MIDNIGHT);

        if (map.get("startTime") != null && map.get("endTime") != null) {
            startTime = DateFormat.toLocalDateTime(map.get("startTime"), true).withHour(0).withMinute(0).minusDays(1);
            endTime = DateFormat.toLocalDateTime(map.get("endTime"), true).withHour(0).withMinute(0);
        }
    }
}
