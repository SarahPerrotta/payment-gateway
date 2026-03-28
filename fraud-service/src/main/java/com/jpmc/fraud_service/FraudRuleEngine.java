package com.jpmc.fraud_service;

import org.springframework.stereotype.Component;

@Component
public class FraudRuleEngine {

    private static final Double HIGH_AMOUNT_THRESHOLD = 1000.0;

    public FraudCheckResponse evaluate(FraudCheckRequest request) {

        if (request.getAmount() > HIGH_AMOUNT_THRESHOLD) {
            return new FraudCheckResponse(
                "FLAGGED",
                "Amount exceeds threshold of £" + HIGH_AMOUNT_THRESHOLD
            );
        }

        if (request.getFromAccount().equals(request.getToAccount())) {
            return new FraudCheckResponse(
                "FLAGGED",
                "Sender and receiver account are the same"
            );
        }

        if (request.getAmount() <= 0) {
            return new FraudCheckResponse(
                "FLAGGED",
                "Invalid amount"
            );
        }

        return new FraudCheckResponse("APPROVED", "Transaction looks good");
    }
}