package com.zinxshosting.backend.instance.config;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Entity
public class TransactionConfiguration {

    @SequenceGenerator(
            name = "transaction_configuration_sequence",
            sequenceName = "transaction_configuration_sequence",
            allocationSize = 1
    )
    @Id
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "transaction_configuration_sequence"
    )
    private Long id;


    @Column(nullable = false)
    private String provider;

    @Column(nullable = false)
    private String type;

    @Column(nullable = false)
    private TransactionList transactionList;

    public TransactionConfiguration(TransactionList transactionList, String type, String provider){
        this.transactionList = transactionList;
        this.type = type;
        this.provider = provider;
    }


}