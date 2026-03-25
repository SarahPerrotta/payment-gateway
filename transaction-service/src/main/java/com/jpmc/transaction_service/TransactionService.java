package com.jpmc.transaction_service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TransactionService {

    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private FxRateClient fxRateClient;

    public Transaction createTransaction(Transaction transaction) {
        if (!transaction.getCurrency().equalsIgnoreCase("GBP")) {
            Double convertedAmount = fxRateClient.convertToGBP(
                transaction.getAmount(), 
                transaction.getCurrency()
            );
            transaction.setAmount(convertedAmount);
            transaction.setCurrency("GBP");
        }
        transaction.setStatus("PENDING");
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