package com.example.airtimemanagement.controller;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UseSerialNumberRequest {
    private String serialNumber;
    private Long distributorId;
}
