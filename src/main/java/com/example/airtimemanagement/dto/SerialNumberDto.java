package com.example.airtimemanagement.dto;

import com.example.airtimemanagement.domain.Region;
import com.example.airtimemanagement.domain.SerialNumberStatus;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class SerialNumberDto {
    private Long id;
    private String serialNumber;
    private Long distributorId;
    private String distributorName;
    private Region distributorRegion;
    private SerialNumberStatus status;
    private LocalDateTime createdAt;
}
