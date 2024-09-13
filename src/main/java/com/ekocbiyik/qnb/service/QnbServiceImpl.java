package com.ekocbiyik.qnb.service;

import com.ekocbiyik.qnb.model.PaymentRequestDTO;
import com.ekocbiyik.qnb.model.QnbParameters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.Instant;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@Service
public class QnbServiceImpl implements IQnbService {

    @Autowired
    private QnbParameters params;

    @Override
    public PaymentRequestDTO getPayment3DFormData(String orderId, Double purchAmount) throws NoSuchAlgorithmException {

        PaymentRequestDTO response = PaymentRequestDTO.builder()
                .orderId(orderId)
                .purchAmount(purchAmount)
                .mbrId(params.getMbrId())
                .merchantID(params.getMerchantID())
                .merchantPass(params.getMerchantPass())
                .userCode(params.getUserCode())
                .userPass(params.getUserPass())
                .currency(params.getCurrency())
                .okUrl(params.getOkUrl())
                .failUrl(params.getFailUrl())
                .secureType("3DHost")
                .txnType("Auth")
                .installmentCount("0")
                .lang(params.getLang())
                .threeDHostUrl(params.getBaseUrl() + "/Gateway/3DHost.aspx")
                .rnd(String.valueOf(Instant.now().getEpochSecond()))
                .build();

        String str = response.getMbrId() +
                response.getOrderId() +
                response.getPurchAmount() +
                response.getOkUrl() +
                response.getFailUrl() +
                response.getTxnType() +
                response.getInstallmentCount() +
                response.getRnd() +
                response.getMerchantPass();

        String hash = Base64
                .getEncoder()
                .encodeToString(
                        MessageDigest
                                .getInstance("SHA-1")
                                .digest(str.getBytes(StandardCharsets.US_ASCII))
                );
        response.setHash(hash);

        return response;
    }

    @Override
    public Map<String, String> paymentCancel(String orderId) {
        try {

            HttpHeaders headers = new HttpHeaders();
            headers.set("Content-Type", "application/x-www-form-urlencoded");

            MultiValueMap<String, String> formData = new LinkedMultiValueMap<>();
            formData.add("MbrId", params.getMbrId());
            formData.add("MerchantID", params.getMerchantID());
            formData.add("UserCode", params.getUserCode());
            formData.add("UserPass", params.getUserPass());
            formData.add("OrderId", orderId);
            formData.add("SecureType", "NonSecure");
            formData.add("TxnType", "Void");
            formData.add("Currency", "949");
            formData.add("Lang", "TR");

            RestTemplate restTemplate = new RestTemplate();
            ResponseEntity<String> response = restTemplate.exchange(
                    params.getBaseUrl() + "/Gateway/Default.aspx",
                    HttpMethod.POST,
                    new HttpEntity<>(formData, headers),
                    String.class
            );

            if (response.getStatusCode() != HttpStatus.OK) {
                if (response.getBody() != null) {
                    throw new Exception(response.getBody());
                }
                throw new Exception("Error occurred during payment cancellation");
            }

            return parseResponse(Objects.requireNonNull(response.getBody()));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Map<String, String> paymentRefund(String orderId, Double amount) {
        try {

            HttpHeaders headers = new HttpHeaders();
            headers.set("Content-Type", "application/x-www-form-urlencoded");

            MultiValueMap<String, String> formData = new LinkedMultiValueMap<>();
            formData.add("MbrId", params.getMbrId());
            formData.add("MerchantID", params.getMerchantID());
            formData.add("UserCode", params.getUserCode());
            formData.add("UserPass", params.getUserPass());
            formData.add("OrderId", orderId);
            formData.add("PurchAmount", amount.toString());
            formData.add("SecureType", "NonSecure");
            formData.add("TxnType", "Refund");
            formData.add("Currency", "949");
            formData.add("Lang", "TR");

            RestTemplate restTemplate = new RestTemplate();
            ResponseEntity<String> response = restTemplate.exchange(
                    params.getBaseUrl() + "/Gateway/Default.aspx",
                    HttpMethod.POST,
                    new HttpEntity<>(formData, headers),
                    String.class
            );

            if (response.getStatusCode() != HttpStatus.OK) {
                if (response.getBody() != null) {
                    throw new Exception(response.getBody());
                }
                throw new Exception("Error occurred during payment cancellation");
            }

            return parseResponse(Objects.requireNonNull(response.getBody()));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Map<String, String> paymentInquiry(String orderId) {
        try {

            HttpHeaders headers = new HttpHeaders();
            headers.set("Content-Type", "application/x-www-form-urlencoded");

            MultiValueMap<String, String> formData = new LinkedMultiValueMap<>();
            formData.add("MbrId", params.getMbrId());
            formData.add("MerchantID", params.getMerchantID());
            formData.add("UserCode", params.getUserCode());
            formData.add("UserPass", params.getUserPass());
            formData.add("OrderId", orderId);
            formData.add("SecureType", "Inquiry");
            formData.add("TxnType", "OrderInquiry");
            formData.add("Lang", "TR");

            RestTemplate restTemplate = new RestTemplate();
            ResponseEntity<String> response = restTemplate.exchange(
                    params.getBaseUrl() + "/Gateway/Default.aspx",
                    HttpMethod.POST,
                    new HttpEntity<>(formData, headers),
                    String.class
            );

            if (response.getStatusCode() != HttpStatus.OK) {
                if (response.getBody() != null) {
                    throw new Exception(response.getBody());
                }
                throw new Exception("Error occurred during payment cancellation");
            }

            return parseResponse(Objects.requireNonNull(response.getBody()));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private Map<String, String> parseResponse(String response) {
        Map<String, String> responseMap = new HashMap<>();
        for (String param : response.split(";;")) {
            String[] nameValue = param.split("=");
            if (nameValue.length == 2) {
                responseMap.put(nameValue[0], nameValue[1]);
            }
        }
        return responseMap;
    }

}
