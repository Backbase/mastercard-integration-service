package com.backbase.mastercard.rest;

import com.backbase.mastercard.service.PaymentOrderService;
import com.backbase.payments.integration.inbound.api.PaymentOrderIntegrationOutboundApi;
import com.backbase.payments.integration.model.CancelResponse;
import com.backbase.payments.integration.model.PaymentOrderPutRequestBody;
import com.backbase.payments.integration.model.PaymentOrderPutResponseBody;
import com.backbase.payments.integration.model.PaymentOrdersPostRequestBody;
import com.backbase.payments.integration.model.PaymentOrdersPostResponseBody;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class PaymentOrderController implements PaymentOrderIntegrationOutboundApi {

    private final PaymentOrderService paymentOrderService;

    @Override
    public ResponseEntity<CancelResponse> postCancelPaymentOrder(String bankReferenceId) {
        return ResponseEntity.ok(paymentOrderService.cancelPaymentOrder(bankReferenceId));
    }

    @Override
    public ResponseEntity<PaymentOrdersPostResponseBody> postPaymentOrders(
        PaymentOrdersPostRequestBody paymentOrdersPostRequestBody) {
        return ResponseEntity.accepted().body(paymentOrderService.createPaymentOrder(paymentOrdersPostRequestBody));
    }

    @Override
    public ResponseEntity<PaymentOrderPutResponseBody> putPaymentOrder(String bankReferenceId,
        PaymentOrderPutRequestBody paymentOrderPutRequestBody) {
        return ResponseEntity.ok(paymentOrderService.updatePaymentOrder(bankReferenceId, paymentOrderPutRequestBody));
    }
}
