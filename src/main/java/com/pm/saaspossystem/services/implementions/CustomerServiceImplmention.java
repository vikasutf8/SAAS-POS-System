package com.pm.saaspossystem.services.implementions;

import com.pm.saaspossystem.mapper.CustomerMapper;
import com.pm.saaspossystem.model.Customer;
import com.pm.saaspossystem.payload.dto.CustomerDto;
import com.pm.saaspossystem.repository.CustomerRepository;
import com.pm.saaspossystem.services.CustomerService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
@RequiredArgsConstructor
public class CustomerServiceImplmention implements CustomerService {
    private final CustomerRepository customerRepository;

    @Override
    public CustomerDto createCustomer(CustomerDto customerDto) {

        Customer customer = CustomerMapper.toEntity(customerDto);
        Customer savedCustomer = customerRepository.save(customer);

        return CustomerMapper.toDto(savedCustomer);
    }

    @Override
    public CustomerDto updateCustomer(Long customerId, CustomerDto customerDto) {

        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new RuntimeException(STR."Customer not found with id : \{customerId}"));

        CustomerMapper.updateCustomer(customer, customerDto);

        Customer updatedCustomer = customerRepository.save(customer);

        return CustomerMapper.toDto(updatedCustomer);
    }

    @Override
    public void deleteCustomer(Long customerId) {

        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new RuntimeException("Customer not found with id : " + customerId));

        customerRepository.delete(customer);
    }

    @Override
    public CustomerDto getCustomerById(Long customerId) {

        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new RuntimeException("Customer not found with id : " + customerId));

        return CustomerMapper.toDto(customer);
    }

    @Override
    public List<CustomerDto> getAllCustomerS() {

        List<Customer> customers = customerRepository.findAll();
        return CustomerMapper.toDtoList(customers);
    }

    @Override
    public List<CustomerDto> searchKeyword(String keyword) {

        List<Customer> customers = customerRepository
                .findByFullNameContainingIgnoreCaseOrEmailContainingIgnoreCase(keyword, keyword);

        return CustomerMapper.toDtoList(customers);
    }
}
