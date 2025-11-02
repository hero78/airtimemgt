package com.example.airtimemanagement.service;

import com.example.airtimemanagement.domain.SerialNumber;

import java.util.List;

public interface SerialNumberService {
    List<SerialNumber> generateSerialNumbers(Long distributorId, int count);
}
