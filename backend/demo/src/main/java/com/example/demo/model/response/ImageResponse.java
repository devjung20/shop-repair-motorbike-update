package com.example.demo.model.response;


import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ImageResponse {
    private Long id;
    private String url;
}
