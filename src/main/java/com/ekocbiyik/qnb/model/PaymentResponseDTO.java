package com.ekocbiyik.qnb.model;

import lombok.Builder;
import lombok.Data;

import java.util.Map;

@Builder
@Data
public class PaymentResponseDTO {
    private String orderId;
    private Boolean result;
    private Map<String, String> responseData;
}