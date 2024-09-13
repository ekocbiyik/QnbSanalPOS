package com.ekocbiyik.qnb.model;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class PaymentRequestDTO {
    private String mbrId;
    private String merchantID;
    private String merchantPass;
    private String userCode;
    private String userPass;
    private String secureType;
    private String txnType;
    private String installmentCount;
    private String currency;
    private String orderId;
    private Double purchAmount;
    private String hash;
    private String rnd;
    private String lang;
    private String host3dUrl;
    private String okUrl;
    private String failUrl;
    private String transactionId;
    private String threeDHostUrl;
}