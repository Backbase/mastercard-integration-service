package com.backbase.mastercard.rest;

import com.backbase.mastercard.service.PaymentOrderService;
import com.backbase.payments.integration.validation.api.PaymentOrderIntegrationValidatorApi;
import com.backbase.payments.integration.validation.api.model.PaymentValidationRequest;
import com.backbase.payments.integration.validation.api.model.PaymentValidationResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class PaymentValidationController implements PaymentOrderIntegrationValidatorApi {

    private final PaymentOrderService paymentOrderService;

    @Override
    public ResponseEntity<PaymentValidationResponse> postWebhookPaymentOrderValidate(
        PaymentValidationRequest paymentValidationRequest) {
        return ResponseEntity.ok(paymentOrderService.validatePaymentOrder(paymentValidationRequest));
    }
}
