package com.zinxshosting.backend.instance.config;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Repository
@Transactional(readOnly = true)
public interface TransactionRepository extends JpaRepository<TransactionConfiguration, Long> {

    Optional<TransactionConfiguration> findById(Long id);

    List<TransactionConfiguration> findByType(String type);

    List<TransactionConfiguration> findByProvider(String provider);

    @Transactional
    @Modifying
    @Query("UPDATE TransactionConfiguration tconfig " +
            "SET tconfig.transactionList = ?2 " +
            "WHERE tconfig = ?1")
    int updateTransactionList(Long id,
                          TransactionList list);





}