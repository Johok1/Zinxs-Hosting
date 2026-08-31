package com.zinxshosting.backend.instance.config;

import jakarta.persistence.Column;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class TransactionConfigurationOVH extends TransactionConfiguration{

    @Column(nullable = false)
    APITransactionListOVH apiTransactionListOVH;

}
