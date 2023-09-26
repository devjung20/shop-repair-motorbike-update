package com.example.demo.model.params;

import com.example.demo.utils.DateFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Map;

@Data
@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor
@AllArgsConstructor
public class StatisticEmployee extends BaseParams {
    private String code;
    private String name;
    private String type;
    private LocalDateTime startTime;
    private LocalDateTime endTime;

    public StatisticEmployee(Map<String ,String> map) {
        super(map);
        this.code = map.get("code");
        this.name = map.get("name");
        this.type = map.get("type") == null ? null : map.get("type").toUpperCase();
        startTime = map.get("startTime") == null ? null : DateFormat.toLocalDateTime(map.get("startTime") , false);
        endTime = map.get("endTime") == null ? null : DateFormat.toLocalDateTime(map.get("endTime"),true);
    }
}
