package com.example.airtimemanagement.repository;

import com.example.airtimemanagement.domain.SerialNumber;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SerialNumberRepository extends JpaRepository<SerialNumber, Long> {
    Optional<SerialNumber> findBySerialNumber(String serialNumber);
    long countByDistributor(com.example.airtimemanagement.domain.Distributor distributor);
}
