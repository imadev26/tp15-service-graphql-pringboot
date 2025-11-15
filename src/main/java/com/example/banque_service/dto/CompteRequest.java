package com.example.banque_service.dto;

import com.example.banque_service.enums.TypeCompte;
import lombok.Data;

@Data
public class CompteRequest {
    private double solde;
    private String dateCreation;
    private TypeCompte type;
}
