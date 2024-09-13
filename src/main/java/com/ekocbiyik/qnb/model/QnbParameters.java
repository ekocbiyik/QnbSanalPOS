package com.ekocbiyik.qnb.model;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Data
@Component
public class QnbParameters {

    @Value("${qnb.mbrId}")
    public String mbrId;

    @Value("${qnb.merchantID}")
    private String merchantID;

    @Value("${qnb.merchantPass}")
    private String merchantPass;

    @Value("${qnb.userCode}")
    private String userCode;

    @Value("${qnb.userPass}")
    private String userPass;

    @Value("${qnb.currency}")
    private String currency;

    @Value("${qnb.okUrl}")
    private String okUrl;

    @Value("${qnb.failUrl}")
    private String failUrl;

    @Value("${qnb.lang}")
    private String lang;

    @Value("${qnb.baseUrl}")
    private String baseUrl;

}