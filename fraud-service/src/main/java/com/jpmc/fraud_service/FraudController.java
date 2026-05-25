package com.jpmc.fraud_service;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/fraud")
@Tag(name = "Fraud Detection", description = "Rule-based fraud detection endpoints")
public class FraudController {

    @Autowired
    private FraudRuleEngine fraudRuleEngine;

    @Operation(summary = "Check a transaction for fraud risk")
    @PostMapping("/check")
    public ResponseEntity<FraudCheckResponse> checkFraud(
            @RequestBody FraudCheckRequest request) {
        FraudCheckResponse response = fraudRuleEngine.evaluate(request);
        return ResponseEntity.ok(response);
    }
}