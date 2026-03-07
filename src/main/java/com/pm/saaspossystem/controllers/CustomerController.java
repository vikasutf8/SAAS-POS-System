package com.pm.saaspossystem.controllers;

import com.pm.saaspossystem.payload.dto.CustomerDto;
import com.pm.saaspossystem.services.CustomerService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/v2/customer")
public class CustomerController {

    private final CustomerService customerService;

    // Create Customer
    @PostMapping
    public CustomerDto createCustomer(@RequestBody CustomerDto customerDto) {
        return customerService.createCustomer(customerDto);
    }

    // Update Customer
    @PutMapping("/{customerId}")
    public CustomerDto updateCustomer(
            @PathVariable Long customerId,
            @RequestBody CustomerDto customerDto
    ) {
        return customerService.updateCustomer(customerId, customerDto);
    }

    // Delete Customer
    @DeleteMapping("/{customerId}")
    public void deleteCustomer(@PathVariable Long customerId) {
        customerService.deleteCustomer(customerId);
    }

    // Get Customer By Id
    @GetMapping("/{customerId}")
    public CustomerDto getCustomerById(@PathVariable Long customerId) {
        return customerService.getCustomerById(customerId);
    }

    // Get All Customers
    @GetMapping
    public List<CustomerDto> getAllCustomers() {
        return customerService.getAllCustomerS();
    }

    // Search Customer
    @GetMapping("/search")
    public List<CustomerDto> searchCustomer(@RequestParam String keyword) {
        return customerService.searchKeyword(keyword);
    }
}
