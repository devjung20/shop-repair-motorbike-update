package com.example.demo.model.params;

import java.util.Map;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor
@AllArgsConstructor
public class ProductParams extends BaseParams {
  private String name;
  private String code;
  private String status;

  public ProductParams(Map<String, String> params) {
    super(params);
    this.name = params.get("name");
    this.code = params.get("code");
    this.status = params.get("status");
  }
}
