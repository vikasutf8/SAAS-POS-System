package com.pm.saaspossystem.controllers;


import com.pm.saaspossystem.domain.OrderStatus;
import com.pm.saaspossystem.domain.PaymentType;
import com.pm.saaspossystem.exceptions.UserExceptions;
import com.pm.saaspossystem.payload.dto.OrderDto;
import com.pm.saaspossystem.services.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/v2/orders")
public class OrderController {

    private final OrderService orderService;

    // Create Order
    @PostMapping
    public OrderDto createOrder(@RequestBody OrderDto orderDto) throws UserExceptions {
        return orderService.createOrder(orderDto);
    }

    // Get Order By Id
    @GetMapping("/{id}")
    public OrderDto getOrderById(@PathVariable Long id) {
        return orderService.getOrderById(id);
    }

    // Get Orders By Branch with filters
    @GetMapping("/branch/{branchId}")
    public List<OrderDto> getOrdersByBranch(
            @PathVariable Long branchId,
            @RequestParam(required = false) Long customerId,
            @RequestParam(required = false) Long cashierId,
            @RequestParam(required = false) PaymentType paymentType,
            @RequestParam(required = false) OrderStatus orderStatus
    ) {
        return orderService.getOrdersByBranch(branchId, customerId, cashierId, paymentType, orderStatus);
    }

    // Get Orders By Cashier
    @GetMapping("/cashier/{cashierId}")
    public List<OrderDto> getOrdersByCashier(@PathVariable Long cashierId) {
        return orderService.getOrdersByCashier(cashierId);
    }

    // Get Orders By Customer
    @GetMapping("/customer/{customerId}")
    public List<OrderDto> getOrdersByCustomer(@PathVariable Long customerId) {
        return orderService.getOrdersByCustomer(customerId);
    }

    // Get Today's Orders By Branch
    @GetMapping("/branch/{branchId}/today")
    public List<OrderDto> getTodayOrdersByBranch(@PathVariable Long branchId) {
        return orderService.getTodayOrdersByBranch(branchId);
    }

    // Get Recent Orders By Branch
    @GetMapping("/branch/{branchId}/recent")
    public List<OrderDto> getRecentOrdersByBranch(@PathVariable Long branchId) {
        return orderService.getRecentOrdersByBranch(branchId);
    }

    // Delete Order
    @DeleteMapping("/{id}")
    public void deleteOrder(@PathVariable Long id) {
        orderService.deleteOrder(id);
    }
}
