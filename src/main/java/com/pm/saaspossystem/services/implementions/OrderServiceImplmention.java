package com.pm.saaspossystem.services.implementions;

import com.pm.saaspossystem.domain.OrderStatus;
import com.pm.saaspossystem.domain.PaymentType;
import com.pm.saaspossystem.exceptions.UserExceptions;
import com.pm.saaspossystem.mapper.OrderMapper;
import com.pm.saaspossystem.model.Branch;
import com.pm.saaspossystem.model.Customer;
import com.pm.saaspossystem.model.Order;
import com.pm.saaspossystem.model.Product;
import com.pm.saaspossystem.payload.dto.OrderDto;
import com.pm.saaspossystem.payload.dto.UserDto;
import com.pm.saaspossystem.repository.BranchRepository;
import com.pm.saaspossystem.repository.CustomerRepository;
import com.pm.saaspossystem.repository.OrderRepository;
import com.pm.saaspossystem.repository.ProductRepository;
import com.pm.saaspossystem.services.OrderService;
import com.pm.saaspossystem.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrderServiceImplmention implements OrderService {

    private final OrderRepository orderRepository;
    private  final UserService userService;
    private  final BranchRepository branchRepository;
    private final ProductRepository productRepository;
    private final CustomerRepository customerRepository;

    @Override
    @Transactional
    public OrderDto createOrder(OrderDto orderDto) throws UserExceptions {
        UserDto cashier = userService.getCurrentUser();

        Branch branch = branchRepository
                .findById(cashier.getBranchId())
                .orElseThrow(() -> new RuntimeException("Branch not found"));

        if (orderDto.getItems() == null || orderDto.getItems().isEmpty()) {
            throw new RuntimeException("Order must contain at least one item");
        }

        Customer customer = customerRepository.findById(orderDto.getCustomerId())
                .orElseThrow(() -> new RuntimeException("Customer not found"));

        // calculate total
        double totalAmount = orderDto.getItems()
                .stream()
                .mapToDouble(item -> {

                    Product product = productRepository.findById(item.getProductId())
                            .orElseThrow(() -> new RuntimeException("Product not found"));

                    double price = product.getSellingPrice();

                    item.setPrice(price); // override frontend price

                    return price * item.getQuantity();

                })
                .sum();

        orderDto.setTotalAmount(totalAmount);

        orderDto.setBranchId(branch.getId());
        orderDto.setCashierId(cashier.getId());
        orderDto.setCustomerId(customer.getId());

        orderDto.setCashier(cashier);
//        orderDto.setBranch(branch);
//        orderDto.setCustomer(customer);


        Order order = OrderMapper.toEntity(orderDto);

        // set order reference in items
        order.getItems().forEach(item -> item.setOrder(order)); // each items setting its order NO its not expensive operation

        Order savedOrder = orderRepository.save(order);

        return OrderMapper.toDto(savedOrder);
    }

    @Override
    public OrderDto getOrderById(Long id) {

        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Order not found"));

        return OrderMapper.toDto(order);
    }

    @Override
    public List<OrderDto> getOrdersByBranch(Long branchId,
                                            Long customerId,
                                            Long cashierId,
                                            PaymentType paymentType,
                                            OrderStatus orderStatus) {

        List<Order> orders = orderRepository.findByBranchId(branchId);

        return orders.stream()
                .filter(o -> customerId == null || (o.getCustomer()!=null && o.getCustomer().getId().equals(customerId)))
                .filter(o -> cashierId == null || (o.getCashier()!=null && o.getCashier().getId().equals(cashierId)))
                .filter(o -> paymentType == null || o.getPaymentType() == paymentType)
//                .filter(o -> orderStatus == null || o.getOrderStatus() == orderStatus) //done or chnage on paymentStatus or shippingStatus
                .map(OrderMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<OrderDto> getOrdersByCashier(Long cashierId) {

        return orderRepository.findByCashierId(cashierId)
                .stream()
                .map(OrderMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public void deleteOrder(Long id) {

        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Order not found"));

        orderRepository.delete(order);
    }

    @Override
    public List<OrderDto> getTodayOrdersByBranch(Long branchId) {

        LocalDateTime start = LocalDate.now().atStartOfDay();
        LocalDateTime end = LocalDate.now().atTime(23, 59, 59);

        List<Order> orders = orderRepository
                .findByBranchIdAndCreatedAtBetween(branchId, start, end);

        return orders.stream()
                .map(OrderMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<OrderDto> getOrdersByCustomer(Long customerId) {

        return orderRepository.findByCustomerId(customerId)
                .stream()
                .map(OrderMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<OrderDto> getRecentOrdersByBranch(Long branchId) {

        return orderRepository
                .findTop5ByBranchIdOrderByCreatedAtDesc(branchId)
                .stream()
                .map(OrderMapper::toDto)
                .collect(Collectors.toList());
    }
}
