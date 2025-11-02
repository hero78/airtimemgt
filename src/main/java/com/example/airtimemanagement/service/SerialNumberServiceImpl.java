package com.example.airtimemanagement.service;

import com.example.airtimemanagement.domain.Distributor;
import com.example.airtimemanagement.domain.SerialNumber;
import com.example.airtimemanagement.domain.SerialNumberStatus;
import com.example.airtimemanagement.repository.DistributorRepository;
import com.example.airtimemanagement.repository.SerialNumberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class SerialNumberServiceImpl implements SerialNumberService {

    private final SerialNumberRepository serialNumberRepository;
    private final DistributorRepository distributorRepository;

    @Transactional
    @Override
    public List<SerialNumber> generateSerialNumbers(Long distributorId, int count) {
        Distributor distributor = distributorRepository.findById(distributorId)
                .orElseThrow(() -> new RuntimeException("Distributor not found"));

        long currentCount = serialNumberRepository.countByDistributor(distributor);
        if (currentCount + count > distributor.getMaxSerialNumbers()) {
            long remaining = distributor.getMaxSerialNumbers() - currentCount;
            throw new RuntimeException("Requested number of serial numbers exceeds the distributor\'s quota. " + remaining + " serial numbers can be generated.");
        }

        List<SerialNumber> serialNumbers = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            SerialNumber serialNumber = new SerialNumber();
            serialNumber.setSerialNumber(generateUniqueSerialNumber(distributor));
            serialNumber.setDistributor(distributor);
            serialNumber.setStatus(SerialNumberStatus.ACTIVE);
            serialNumber.setCreatedAt(LocalDateTime.now());
            serialNumbers.add(serialNumber);
        }

        return serialNumberRepository.saveAll(serialNumbers);
    }

    private String generateUniqueSerialNumber(Distributor distributor) {
        // Encode region and distributor information into the serial number
        String regionCode = distributor.getRegion().name().substring(0, 3);
        String distributorId = String.format("%04d", distributor.getId());
        String uniquePart = UUID.randomUUID().toString().substring(0, 8);
        return regionCode + "-" + distributorId + "-" + uniquePart;
    }
}
