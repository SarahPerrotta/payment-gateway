package com.jpmc.fraud_service;

public class FraudCheckResponse {
    private String result;
    private String reason;

    public FraudCheckResponse(String result, String reason) {
        this.result = result;
        this.reason = reason;
    }

    public String getResult() { return result; }
    public void setResult(String result) { this.result = result; }

    public String getReason() { return reason; }
    public void setReason(String reason) { this.reason = reason; }
}