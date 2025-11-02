package com.example.airtimemanagement.service;

import com.example.airtimemanagement.controller.SerialNumberFilter;
import com.example.airtimemanagement.domain.SerialNumber;
import com.example.airtimemanagement.dto.SerialNumberDto;

import java.util.List;

public interface SerialNumberService {
    List<SerialNumber> generateSerialNumbers(Long distributorId, int count);
    SerialNumber useSerialNumber(String serialNumber, Long distributorId);
    List<SerialNumberDto> getSerialNumbers(SerialNumberFilter filter);
}
