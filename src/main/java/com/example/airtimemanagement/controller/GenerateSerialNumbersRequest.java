package com.example.airtimemanagement.controller;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GenerateSerialNumbersRequest {
    private Long distributorId;
    private int count;
}
