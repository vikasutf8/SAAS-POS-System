package com.pm.saaspossystem.services;

import com.pm.saaspossystem.domain.OrderStatus;
import com.pm.saaspossystem.domain.PaymentType;
import com.pm.saaspossystem.exceptions.UserExceptions;
import com.pm.saaspossystem.payload.dto.OrderDto;

import java.util.List;

public interface OrderService {

    OrderDto createOrder(OrderDto orderDto) throws UserExceptions;
    OrderDto getOrderById(Long id);
    List<OrderDto> getOrdersByBranch(Long branchId, Long customerId, Long cashierId, PaymentType paymentType, OrderStatus orderStatus);
    List<OrderDto> getOrdersByCashier(Long cashierId);

    void deleteOrder(Long id);

    List<OrderDto>getTodayOrdersByBranch(Long branchId);

    List<OrderDto> getOrdersByCustomer(Long customerId);

//    List<OrderDto> getOrdersByBranchAndDateRange(Long branchId, String from, String to);
//    List<OrderDto> getOrdersByCashierAndDateRange(Long cashierId, String from, String to);


    List<OrderDto> getRecentOrdersByBranch(Long branchId);
}
