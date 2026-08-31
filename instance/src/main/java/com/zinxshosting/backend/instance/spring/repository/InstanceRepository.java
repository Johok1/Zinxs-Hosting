package com.zinxshosting.backend.instance.spring.repository;

import com.zinxshosting.backend.instance.spring.entity.InstanceConfiguration;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Repository
@Transactional(readOnly = true)
public interface InstanceRepository extends JpaRepository<InstanceConfiguration, Long> {
    Optional<InstanceConfiguration> findByid(Long id);


    @Transactional
    @Modifying
    @Query("UPDATE InstanceConfiguration instance " +
            "SET instance.password = ?2 " +
            "WHERE instance = ?1")
    int updatePassword(Long id,
                       String password);


    @Transactional
    @Modifying
    @Query("UPDATE InstanceConfiguration instance " +
            "SET instance.hostname = ?2 " +
            "WHERE instance = ?1")
    int updateHostname(Long id,
                       String hostname);

    @Transactional
    @Modifying
    @Query("UPDATE InstanceConfiguration instance " +
            "SET instance.username = ?2 " +
            "WHERE instance = ?1")
    int updateUsername(Long id,
                       String username);

    @Transactional
    @Modifying
    @Query("UPDATE InstanceConfiguration instance " +
            "SET instance.port = ?2 " +
            "WHERE instance = ?1")
    int updatePort(Long id,
                       Integer port);

}

