package com.example.airtimemanagement.controller;

import com.example.airtimemanagement.dto.SerialNumberDto;
import com.example.airtimemanagement.service.SerialNumberService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/serial-numbers")
@RequiredArgsConstructor
@Tag(name = "Serial Number Management", description = "APIs for generating and managing serial numbers")
public class SerialNumberController {

    private final SerialNumberService serialNumberService;

    @Operation(summary = "Fetch serial numbers with optional filters")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully fetched serial numbers")
    })
    @GetMapping
    public List<SerialNumberDto> getSerialNumbers(SerialNumberFilter filter) {
        return serialNumberService.getSerialNumbers(filter);
    }

    @Operation(summary = "Generate new serial numbers for a distributor")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully generated serial numbers"),
            @ApiResponse(responseCode = "400", description = "Bad request, e.g., distributor not found or quota exceeded")
    })
    @PostMapping("/generate")
    public List<com.example.airtimemanagement.domain.SerialNumber> generateSerialNumbers(@RequestBody GenerateSerialNumbersRequest request) {
        return serialNumberService.generateSerialNumbers(request.getDistributorId(), request.getCount());
    }

    @Operation(summary = "Mark a serial number as used by a distributor")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully marked serial number as used"),
            @ApiResponse(responseCode = "400", description = "Bad request, e.g., serial number not found, not active, or does not belong to the distributor")
    })
    @PostMapping("/use")
    public com.example.airtimemanagement.domain.SerialNumber useSerialNumber(@RequestBody UseSerialNumberRequest request) {
        return serialNumberService.useSerialNumber(request.getSerialNumber(), request.getDistributorId());
    }
}
