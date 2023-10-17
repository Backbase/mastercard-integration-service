package com.backbase.mastercard.service;

import com.backbase.payments.integration.model.CancelResponse;
import com.backbase.payments.integration.model.PaymentOrderPutRequestBody;
import com.backbase.payments.integration.model.PaymentOrderPutResponseBody;
import com.backbase.payments.integration.model.PaymentOrdersPostRequestBody;
import com.backbase.payments.integration.model.PaymentOrdersPostResponseBody;
import com.backbase.payments.integration.validation.api.model.PaymentValidationRequest;
import com.backbase.payments.integration.validation.api.model.PaymentValidationResponse;
import java.util.UUID;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class PaymentOrderService {

    public CancelResponse cancelPaymentOrder(String bankReferenceId) {
        log.debug("Canceling payment: {}", bankReferenceId);
        return new CancelResponse().accepted(true);
    }

    public PaymentOrdersPostResponseBody createPaymentOrder(PaymentOrdersPostRequestBody paymentOrdersPostRequestBody) {
        log.debug("Creating payment: {}", paymentOrdersPostRequestBody);
        String bankReferenceId = UUID.randomUUID().toString();
        return new PaymentOrdersPostResponseBody()
            .bankReferenceId(bankReferenceId)
            .bankStatus("ACCEPTED");
    }

    public PaymentOrderPutResponseBody updatePaymentOrder(String bankReferenceId,
        PaymentOrderPutRequestBody paymentOrderPutRequestBody) {
        log.debug("Updating payment: {}", bankReferenceId);
        return new PaymentOrderPutResponseBody()
            .bankReferenceId(bankReferenceId)
            .bankStatus("ACCEPTED");
    }

    public PaymentValidationResponse validatePaymentOrder(PaymentValidationRequest paymentValidationRequest) {
        log.debug("Validating payment: {}", paymentValidationRequest);
        return new PaymentValidationResponse().message("VALIDATED");
    }
}
