package com.jpmc.transaction_service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;

@Service
public class TransactionService {

    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private FxRateClient fxRateClient;

    private final RestTemplate restTemplate = new RestTemplate();
    private final String FRAUD_SERVICE_URL = "http://localhost:8083/fraud/check";

    public Transaction createTransaction(Transaction transaction) {
        // Step 1: Convert currency if not GBP
        if (!transaction.getCurrency().equalsIgnoreCase("GBP")) {
            Double convertedAmount = fxRateClient.convertToGBP(
                transaction.getAmount(),
                transaction.getCurrency()
            );
            transaction.setAmount(convertedAmount);
            transaction.setCurrency("GBP");
        }

        // Step 2: Call fraud-service
        Map<String, String> fraudRequest = Map.of(
            "fromAccount", transaction.getFromAccount(),
            "toAccount", transaction.getToAccount(),
            "amount", transaction.getAmount().toString(),
            "currency", transaction.getCurrency()
        );

        Map<String, String> fraudResponse = restTemplate.postForObject(
            FRAUD_SERVICE_URL,
            fraudRequest,
            Map.class
        );

        // Step 3: Set status based on fraud check result
        if (fraudResponse != null && "FLAGGED".equals(fraudResponse.get("result"))) {
            transaction.setStatus("FLAGGED");
        } else {
            transaction.setStatus("APPROVED");
        }

        // Step 4: Save and return
        return transactionRepository.save(transaction);
    }

    public Transaction getTransaction(Long id) {
        return transactionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Transaction not found"));
    }

    public List<Transaction> getAllTransactions() {
        return transactionRepository.findAll();
    }

    public List<Transaction> getTransactionsByAccount(String fromAccount) {
        return transactionRepository.findByFromAccount(fromAccount);
    }
}