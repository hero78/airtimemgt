package com.example.airtimemanagement.controller;

import com.example.airtimemanagement.domain.SerialNumber;
import com.example.airtimemanagement.service.SerialNumberService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/serial-numbers")
@RequiredArgsConstructor
public class SerialNumberController {

    private final SerialNumberService serialNumberService;

    @PostMapping("/generate")
    public List<SerialNumber> generateSerialNumbers(@RequestBody GenerateSerialNumbersRequest request) {
        return serialNumberService.generateSerialNumbers(request.getDistributorId(), request.getCount());
    }
}
