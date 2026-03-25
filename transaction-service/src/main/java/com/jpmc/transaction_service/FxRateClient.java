package com.jpmc.transaction_service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@Component
public class FxRateClient {

    @Value("${fx.api.key}")
    private String apiKey;

    private final RestTemplate restTemplate = new RestTemplate();

    public Double convertToGBP(Double amount, String fromCurrency) {
        if (fromCurrency.equalsIgnoreCase("GBP")) {
            return amount;
        }

        String url = "http://api.exchangeratesapi.io/v1/latest?access_key=" 
                     + apiKey + "&base=EUR&symbols=GBP," + fromCurrency;

        Map<String, Object> response = restTemplate.getForObject(url, Map.class);

        if (response != null && response.containsKey("rates")) {
            Map<String, Double> rates = (Map<String, Double>) response.get("rates");
            Double toGBP = rates.get("GBP");
            Double fromRate = rates.get(fromCurrency.toUpperCase());

            if (toGBP != null && fromRate != null) {
                Double amountInEur = amount / fromRate;
                return amountInEur * toGBP;
            }
        }
        return amount;
    }
}
