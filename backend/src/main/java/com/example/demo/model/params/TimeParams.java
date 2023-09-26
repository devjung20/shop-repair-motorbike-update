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
public class TimeParams {
    private LocalDateTime startTime;
    private LocalDateTime endTime;

    public TimeParams(Map<String, String> map) {
        startTime = map.get("startTime") == null ? null : DateFormat.toLocalDateTimeInParam(map.get("startTime"));
        endTime = map.get("endTime") == null ? null : DateFormat.toLocalDateTimeInParam(map.get("endTime")).plusDays(1);
    }
}
