package com.pm.saaspossystem.mapper;

import com.pm.saaspossystem.model.Customer;
import com.pm.saaspossystem.payload.dto.CustomerDto;

import java.util.List;
import java.util.stream.Collectors;

public class CustomerMapper {


    // Entity -> DTO
    public static CustomerDto toDto(Customer customer) {
        if (customer == null) {
            return null;
        }

        return CustomerDto.builder()
                .id(customer.getId())
                .fullName(customer.getFullName())
                .email(customer.getEmail())
                .phone(customer.getPhone())
                .build();
    }

    // DTO -> Entity
    public static Customer toEntity(CustomerDto dto) {
        if (dto == null) {
            return null;
        }

        return Customer.builder()
                .id(dto.getId())
                .fullName(dto.getFullName())
                .email(dto.getEmail())
                .phone(dto.getPhone())
                .build();
    }

    // Update existing entity from DTO
    public static void updateCustomer(Customer customer, CustomerDto dto) {

        if (dto.getFullName() != null) {
            customer.setFullName(dto.getFullName());
        }

        if (dto.getEmail() != null) {
            customer.setEmail(dto.getEmail());
        }

        if (dto.getPhone() != null) {
            customer.setPhone(dto.getPhone());
        }
    }

    // Entity List -> DTO List
    public static List<CustomerDto> toDtoList(List<Customer> customers) {
        return customers.stream()
                .map(CustomerMapper::toDto)
                .collect(Collectors.toList());
    }
}
