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
public class StatisticServiceParams extends BaseParams {
    private String code;
    private String name;
    private LocalDateTime startTime;
    private LocalDateTime endTime;

    public StatisticServiceParams(Map<String, String> map) {
        super(map);
        this.code = map.get("code");
        this.name = map.get("name");

        if (map.get("startTime") != null && map.get("endTime") != null) {
            startTime = DateFormat.toLocalDateTime(map.get("startTime"), true).withHour(0).withMinute(0).minusDays(1);
            endTime = DateFormat.toLocalDateTime(map.get("endTime"), true).withHour(0).withMinute(0);
        }
    }
}
