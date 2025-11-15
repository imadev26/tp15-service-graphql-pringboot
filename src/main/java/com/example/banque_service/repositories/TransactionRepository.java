package com.example.banque_service.repositories;

import com.example.banque_service.entities.Compte;
import com.example.banque_service.entities.Transaction;
import com.example.banque_service.enums.TypeTransaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long> {
    
    List<Transaction> findByCompte(Compte compte);
    
    @Query("SELECT COALESCE(SUM(t.montant), 0) FROM Transaction t WHERE t.type = :type")
    double sumByType(@Param("type") TypeTransaction type);
}
