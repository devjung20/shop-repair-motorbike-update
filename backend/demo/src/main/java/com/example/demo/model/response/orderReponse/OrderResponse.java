package com.example.demo.model.response.orderReponse;

import com.example.demo.model.response.StoreResponse;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
public class OrderResponse {
    private Long id;
    private String code;
    private LocalDateTime modifiedDate;
    private InfoCustomer infoCustomer;
    private InfoEmployee infoDispatcher;
    private InfoEmployee infoRepairer;
    private String note;
    private List<InfoService> infoServices;
    private List<InfoProduct> infoProducts;
    private StoreResponse storeResponse;
}
