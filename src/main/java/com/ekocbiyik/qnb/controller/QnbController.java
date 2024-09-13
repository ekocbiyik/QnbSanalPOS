package com.ekocbiyik.qnb.controller;

import com.ekocbiyik.qnb.model.PaymentRequestDTO;
import com.ekocbiyik.qnb.model.PaymentResponseDTO;
import com.ekocbiyik.qnb.service.IQnbService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
public class QnbController {

    @Autowired
    private IQnbService qnbService;

    @GetMapping("/initiate")
    public ResponseEntity<String> initiatePayment(@RequestParam Double amount) {

        try {

            PaymentRequestDTO payment = qnbService.getPayment3DFormData(UUID.randomUUID().toString(), amount);

            String form = "<html>" +
                    "<body>" +
                    "   <form id='paymentForm' action='" + payment.getThreeDHostUrl() + "' method='post'>" +
                    "       <input type='hidden' name='MbrId' value='" + payment.getMbrId() + "' />" +
                    "       <input type='hidden' name='MerchantID' value='" + payment.getMerchantID() + "' />" +
                    "       <input type='hidden' name='UserCode' value='" + payment.getUserCode() + "' />" +
                    "       <input type='hidden' name='UserPass' value='" + payment.getUserPass() + "' />" +
                    "       <input type='hidden' name='SecureType' value='" + payment.getSecureType() + "' />" +
                    "       <input type='hidden' name='TxnType' value='" + payment.getTxnType() + "' />" +
                    "       <input type='hidden' name='InstallmentCount' value='" + payment.getInstallmentCount() + "' />" +
                    "       <input type='hidden' name='Currency' value='" + payment.getCurrency() + "' />" +
                    "       <input type='hidden' name='OkUrl' value='" + payment.getOkUrl() + "' />" +
                    "       <input type='hidden' name='FailUrl' value='" + payment.getFailUrl() + "' />" +
                    "       <input type='hidden' name='OrderId' value='" + payment.getOrderId() + "' />" +
                    "       <input type='hidden' name='PurchAmount' value='" + payment.getPurchAmount() + "' />" +
                    "       <input type='hidden' name='Lang' value='" + payment.getLang() + "' />" +
                    "       <input type='hidden' name='Rnd' value='" + payment.getRnd() + "' />" +
                    "       <input type='hidden' name='Hash' value='" + payment.getHash() + "' />" +
                    "   </form>" +
                    "   <script type='text/javascript'>" +
                    "       document.getElementById('paymentForm').submit();" +
                    "   </script>" +
                    "</body>" +
                    "</html>";

            return ResponseEntity
                    .ok()
                    .contentType(MediaType.TEXT_HTML)
                    .body(form);
        } catch (Exception ex) {
            return ResponseEntity.badRequest().body("{\"message\": \"An error occurred: " + ex.getMessage() + "\"}");
        }
    }

    @PostMapping("/3dsecure-response")
    public ResponseEntity<PaymentResponseDTO> handle3Dresponse(HttpServletRequest request) {

        Map<String, String> formData = request
                .getParameterMap()
                .entrySet()
                .stream()
                .collect(Collectors.toMap(Map.Entry::getKey, e -> e.getValue()[0]));

        PaymentResponseDTO result = PaymentResponseDTO.builder()
                .orderId(formData.get("OrderId"))
                .responseData(formData)
                .result(Objects.equals("00", formData.get("ProcReturnCode")))
                .build();

        return ResponseEntity
                .ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(result);
    }

    @GetMapping("/cancel")
    public ResponseEntity<PaymentResponseDTO> cancelPayment(@RequestParam String orderId) {

        Map<String, String> formData = qnbService.paymentCancel(orderId);

        PaymentResponseDTO result = PaymentResponseDTO.builder()
                .orderId(formData.get("OrderId"))
                .responseData(formData)
                .result(Objects.equals("00", formData.get("ProcReturnCode")))
                .build();

        return ResponseEntity
                .ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(result);
    }

    @GetMapping("/refund")
    public ResponseEntity<PaymentResponseDTO> refundPayment(@RequestParam String orderId, @RequestParam Double amount) {

        Map<String, String> formData = qnbService.paymentRefund(orderId, amount);

        PaymentResponseDTO result = PaymentResponseDTO.builder()
                .orderId(formData.get("OrderId"))
                .responseData(formData)
                .result(Objects.equals("00", formData.get("ProcReturnCode")))
                .build();

        return ResponseEntity
                .ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(result);
    }

    @GetMapping("/inquiry")
    public ResponseEntity<PaymentResponseDTO> inquiryPayment(@RequestParam String orderId) {

        Map<String, String> formData = qnbService.paymentInquiry(orderId);

        PaymentResponseDTO result = PaymentResponseDTO.builder()
                .orderId(formData.get("OrderId"))
                .responseData(formData)
                .result(Objects.equals("00", formData.get("ProcReturnCode")))
                .build();

        return ResponseEntity
                .ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(result);
    }

}
