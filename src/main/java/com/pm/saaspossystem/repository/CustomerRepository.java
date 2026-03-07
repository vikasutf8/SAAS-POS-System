package com.pm.saaspossystem.repository;

import com.pm.saaspossystem.model.Customer;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CustomerRepository extends JpaRepository<Customer,Long> {


    List<Customer> findByFullNameContainingIgnoreCaseOrEmailContainingIgnoreCase(String keyword, String keyword1);
}
