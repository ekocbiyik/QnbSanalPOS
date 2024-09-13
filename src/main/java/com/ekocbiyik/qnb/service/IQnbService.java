package com.ekocbiyik.qnb.service;

import com.ekocbiyik.qnb.model.PaymentRequestDTO;

import java.security.NoSuchAlgorithmException;
import java.util.Map;

public interface IQnbService {

    PaymentRequestDTO getPayment3DFormData(String orderId, Double purchAmount) throws NoSuchAlgorithmException;

    Map<String, String> paymentCancel(String orderId);

    Map<String, String> paymentRefund(String orderId, Double amount);

    Map<String, String> paymentInquiry(String orderId);

}
