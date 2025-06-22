package com.harington.devops_training.kafka.converter;

import com.harington.devops_training.kafka.model.ContractDto;

import java.util.List;
import java.util.stream.Collectors;

public class ContractConverter {
//
//    public static ContractAvro convertToAvro(ContractDto dto) {
//        return ContractAvro.newBuilder()
//                .setId(dto.getId())
//                .setAmount(dto.getAmount())
//                .setIsEligible(dto.isEligible())
//                .build();
//    }
//
//    public static List<ContractAvro> convertListToAvro(List<ContractDto> dtos) {
//        return dtos.stream()
//                .map(ContractConverter::convertToAvro)
//                .collect(Collectors.toList());
//    }
}