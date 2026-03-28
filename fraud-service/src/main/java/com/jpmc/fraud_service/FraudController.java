package com.jpmc.fraud_service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/fraud")
public class FraudController {

    @Autowired
    private FraudRuleEngine fraudRuleEngine;

    @PostMapping("/check")
    public ResponseEntity<FraudCheckResponse> checkFraud(
            @RequestBody FraudCheckRequest request) {
        FraudCheckResponse response = fraudRuleEngine.evaluate(request);
        return ResponseEntity.ok(response);
    }
}