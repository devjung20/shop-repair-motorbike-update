package com.example.demo.model.params;

import java.util.Map;

import org.springframework.data.domain.Sort.Direction;

import com.google.common.base.CaseFormat;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class BaseParams {
    private int pageNumber = 1;
    private int pageSize = 10;
    private String sortBy = "id";
    private Direction sortOrder = Direction.DESC;

    public BaseParams(Map<String, String> params) {
        if (params.get("pageNumber") != null) {
            this.pageNumber = Integer.parseInt(params.get("pageNumber"));
        }
        if (params.get("pageSize") != null) {
            this.pageSize = Integer.parseInt(params.get("pageSize"));
        }
        if (params.get("sortBy") != null) {
            this.sortBy = CaseFormat.LOWER_CAMEL.to(CaseFormat.LOWER_UNDERSCORE, params.get("sortBy"));
        }
        if (params.get("sortOrder") != null && "ascend".equalsIgnoreCase(params.get("sortOrder"))) {
            this.sortOrder = Direction.ASC;
        }
    }
}
