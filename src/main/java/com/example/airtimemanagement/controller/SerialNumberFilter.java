package com.example.airtimemanagement.controller;

import com.example.airtimemanagement.domain.SerialNumberStatus;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class SerialNumberFilter {
    private String serialNumber;
    private Long distributorId;
    private SerialNumberStatus status;
    private LocalDate createdAtFrom;
    private LocalDate createdAtTo;
}
