package com.example.airtimemanagement.service;

import com.example.airtimemanagement.controller.SerialNumberFilter;
import com.example.airtimemanagement.domain.Distributor;
import com.example.airtimemanagement.domain.Region;
import com.example.airtimemanagement.domain.SerialNumber;
import com.example.airtimemanagement.domain.SerialNumberStatus;
import com.example.airtimemanagement.dto.SerialNumberDto;
import com.example.airtimemanagement.jooq.tables.records.SerialNumbersRecord;
import com.example.airtimemanagement.repository.DistributorRepository;
import com.example.airtimemanagement.repository.SerialNumberRepository;
import lombok.RequiredArgsConstructor;
import org.jooq.Condition;
import org.jooq.DSLContext;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static com.example.airtimemanagement.jooq.tables.Distributors.DISTRIBUTORS;
import static com.example.airtimemanagement.jooq.tables.SerialNumbers.SERIAL_NUMBERS;
import static org.jooq.impl.DSL.trueCondition;

@Service
@RequiredArgsConstructor
public class SerialNumberServiceImpl implements SerialNumberService {

    private final SerialNumberRepository serialNumberRepository;
    private final DistributorRepository distributorRepository;
    private final DSLContext dsl;

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

    @Transactional
    @Override
    public SerialNumber useSerialNumber(String serialNumberValue, Long distributorId) {
        SerialNumber serialNumber = serialNumberRepository.findBySerialNumber(serialNumberValue)
                .orElseThrow(() -> new RuntimeException("Serial number not found"));

        if (!serialNumber.getDistributor().getId().equals(distributorId)) {
            throw new RuntimeException("Serial number does not belong to this distributor");
        }

        if (serialNumber.getStatus() != SerialNumberStatus.ACTIVE) {
            throw new RuntimeException("Serial number is not active");
        }

        serialNumber.setStatus(SerialNumberStatus.USED);
        return serialNumberRepository.save(serialNumber);
    }

    @Override
    public List<SerialNumberDto> getSerialNumbers(SerialNumberFilter filter) {
        Condition condition = trueCondition();
        if (filter.getSerialNumber() != null) {
            condition = condition.and(SERIAL_NUMBERS.SERIAL_NUMBER.eq(filter.getSerialNumber()));
        }
        if (filter.getDistributorId() != null) {
            condition = condition.and(SERIAL_NUMBERS.DISTRIBUTOR_ID.eq(filter.getDistributorId()));
        }
        if (filter.getStatus() != null) {
            condition = condition.and(SERIAL_NUMBERS.STATUS.eq(filter.getStatus().name()));
        }
        if (filter.getCreatedAtFrom() != null) {
            condition = condition.and(SERIAL_NUMBERS.CREATED_AT.ge(filter.getCreatedAtFrom().atStartOfDay()));
        }
        if (filter.getCreatedAtTo() != null) {
            condition = condition.and(SERIAL_NUMBERS.CREATED_AT.le(filter.getCreatedAtTo().atStartOfDay().plusDays(1)));
        }

        return dsl.select(
                        SERIAL_NUMBERS.ID,
                        SERIAL_NUMBERS.SERIAL_NUMBER,
                        SERIAL_NUMBERS.DISTRIBUTOR_ID,
                        DISTRIBUTORS.NAME,
                        DISTRIBUTORS.REGION,
                        SERIAL_NUMBERS.STATUS,
                        SERIAL_NUMBERS.CREATED_AT
                )
                .from(SERIAL_NUMBERS)
                .join(DISTRIBUTORS).on(SERIAL_NUMBERS.DISTRIBUTOR_ID.eq(DISTRIBUTORS.ID))
                .where(condition)
                .fetch(r -> SerialNumberDto.builder()
                        .id(r.get(SERIAL_NUMBERS.ID))
                        .serialNumber(r.get(SERIAL_NUMBERS.SERIAL_NUMBER))
                        .distributorId(r.get(SERIAL_NUMBERS.DISTRIBUTOR_ID))
                        .distributorName(r.get(DISTRIBUTORS.NAME))
                        .distributorRegion(Region.valueOf(r.get(DISTRIBUTORS.REGION)))
                        .status(SerialNumberStatus.valueOf(r.get(SERIAL_NUMBERS.STATUS)))
                        .createdAt(r.get(SERIAL_NUMBERS.CREATED_AT))
                        .build());
    }

    private String generateUniqueSerialNumber(Distributor distributor) {
        // Encode region and distributor information into the serial number
        String regionCode = distributor.getRegion().name().substring(0, 3);
        String distributorId = String.format("%04d", distributor.getId());
        String uniquePart = UUID.randomUUID().toString().substring(0, 8);
        return regionCode + "-" + distributorId + "-" + uniquePart;
    }
}
