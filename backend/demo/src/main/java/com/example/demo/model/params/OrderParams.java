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
public class OrderParams extends BaseParams {
  private String code;
  private String customerCode;
  private String customerName;
  private String repairerName;
  private String dispatcherName;
  private String motorbikeCode;
  private String type;
  private LocalDateTime startTime;
  private LocalDateTime endTime;

  public OrderParams(Map<String, String> map) {
    super(map);
    this.code = map.get("code");
    this.customerCode = map.get("customerCode");
    this.customerName = map.get("customerName");
    this.dispatcherName = map.get("dispatcherName");
    this.repairerName = map.get("repairerName");
    this.motorbikeCode = map.get("motorbikeCode");
    this.type = map.get("type");

    if (map.get("startTime") != null && map.get("endTime") != null) {
      startTime = DateFormat.toLocalDateTime(map.get("startTime"), true).withHour(0).withMinute(0).minusDays(1);
      endTime = DateFormat.toLocalDateTime(map.get("endTime"), true).withHour(0).withMinute(0);
    }
  }
}