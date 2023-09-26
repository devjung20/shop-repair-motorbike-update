package com.example.demo.model.params;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.Map;

@Data
@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor
@AllArgsConstructor
public class EmployeeParams extends BaseParams {
  private String name;
  private String code;
  private String phone;
  private String email;
  private String type;
  private Boolean status;
//  private Boolean isWorking;

  public EmployeeParams(Map<String, String> params) {
    super(params);
    this.name = params.get("name");
    this.code = params.get("code");
    this.phone = params.get("phone");
    this.email = params.get("email");
    this.type = params.get("type");
    if (params.get("status") != null) {
      this.status = Boolean.valueOf(params.get("status"));
    }
//    if (params.get("isWorking") != null) {
//      this.isWorking = Boolean.valueOf(params.get("isWorking"));
//    }
  }
}
