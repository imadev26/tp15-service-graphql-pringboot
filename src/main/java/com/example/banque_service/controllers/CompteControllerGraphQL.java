package com.example.banque_service.controllers;

import com.example.banque_service.dto.TransactionRequest;
import com.example.banque_service.entities.Compte;
import com.example.banque_service.entities.Transaction;
import com.example.banque_service.enums.TypeTransaction;
import com.example.banque_service.repositories.CompteRepository;
import com.example.banque_service.repositories.TransactionRepository;
import lombok.AllArgsConstructor;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Map;

@Controller
@AllArgsConstructor
public class CompteControllerGraphQL {
    private CompteRepository compteRepository;
    private TransactionRepository transactionRepository;
    
    @QueryMapping
    public List<Compte> allComptes(){
        return compteRepository.findAll();
    }
    
    @QueryMapping
    public Compte compteById(@Argument Long id){
        Compte compte = compteRepository.findById(id).orElse(null);
        if(compte == null) throw new RuntimeException(String.format("Compte %s not found", id));
        else return compte;
    }
    
    @MutationMapping
    public Compte saveCompte(@Argument Compte compte){
        return compteRepository.save(compte);
    }
    
    @QueryMapping
    public Map<String, Object> totalSolde() {
        long count = compteRepository.count(); 
        double sum = compteRepository.sumSoldes();
        double average = count > 0 ? sum / count : 0;
        
        return Map.of(
            "count", count,
            "sum", sum,
            "average", average
        );
    }
    
    // Transaction methods
    
    @MutationMapping
    public Transaction addTransaction(@Argument TransactionRequest transaction) {
        Compte compte = compteRepository.findById(transaction.getCompteId())
            .orElseThrow(() -> new RuntimeException("Compte not found"));
        
        Transaction newTransaction = new Transaction();
        newTransaction.setMontant(transaction.getMontant());
        
        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
            newTransaction.setDate(dateFormat.parse(transaction.getDate()));
        } catch (ParseException e) {
            throw new RuntimeException("Invalid date format. Use yyyy/MM/dd");
        }
        
        newTransaction.setType(transaction.getType());
        newTransaction.setCompte(compte);
        
        return transactionRepository.save(newTransaction);
    }
    
    @QueryMapping
    public List<Transaction> compteTransactions(@Argument Long id) {
        Compte compte = compteRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Compte not found"));
        return transactionRepository.findByCompte(compte);
    }
    
    @QueryMapping
    public List<Transaction> allTransactions() {
        return transactionRepository.findAll();
    }
    
    @QueryMapping
    public Map<String, Object> transactionStats() {
        long count = transactionRepository.count();
        double sumDepots = transactionRepository.sumByType(TypeTransaction.DEPOT);
        double sumRetraits = transactionRepository.sumByType(TypeTransaction.RETRAIT);
        
        return Map.of(
            "count", count,
            "sumDepots", sumDepots,
            "sumRetraits", sumRetraits
        );
    }
}
