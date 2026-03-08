package com.pm.saaspossystem.services.implementions;

import com.pm.saaspossystem.domain.PaymentType;
import com.pm.saaspossystem.exceptions.UserExceptions;
import com.pm.saaspossystem.mapper.ShiftReportMapper;
import com.pm.saaspossystem.mapper.UserMapper;
import com.pm.saaspossystem.model.*;
import com.pm.saaspossystem.payload.dto.ShiftReportDto;
import com.pm.saaspossystem.payload.dto.UserDto;
import com.pm.saaspossystem.repository.*;
import com.pm.saaspossystem.services.ShiftReportService;
import com.pm.saaspossystem.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
public class ShiftReportServiceImplmention implements ShiftReportService {

    private final ShiftReportRespository shiftReportRepository;
    private final BranchRepository branchRepository;
    private final UserService userService;
    private final RefundRepository refundRepository;
    private final OrderRepository orderRepository;
    private  final UserRepository userRepository;

    @Override
    public ShiftReportDto startShift(Long cashierId, Long branchId, LocalDateTime shiftStartTime) throws Exception {

        UserDto cashier = userService.getCurrentUser();
        shiftStartTime =LocalDateTime.now();
        LocalDateTime startOfDay = shiftStartTime.withHour(0).withMinute(0).withSecond(0);
        LocalDateTime endOfDay = shiftStartTime.withHour(23).withMinute(59).withSecond(59);

        Branch branch = branchRepository.findById(branchId)
                .orElseThrow(() -> new Exception("Branch not found"));


        Optional<ShiftReport> existingShift = shiftReportRepository.findByCashierIdAndShiftStartBetween(
                cashier.getId(),
                startOfDay,
                endOfDay
        );
        if(existingShift.isPresent()){
            throw new Exception("Shift already present for today");
        }


        ShiftReport shiftReport = new ShiftReport();
        shiftReport.setCashier(UserMapper.toEntity(cashier));
        shiftReport.setBranch(branch);
        shiftReport.setShiftStart(shiftStartTime);

        shiftReportRepository.save(shiftReport);

        return ShiftReportMapper.toDto(shiftReport);
    }

    @Override
    public ShiftReportDto endShift(Long shiftReportId, LocalDateTime shiftEndTime) throws Exception {
        UserDto cashier = userService.getCurrentUser();

        ShiftReport shiftReport = shiftReportRepository.findTopByCashierAndShiftEndIsNullOrderByShiftStartDesc(UserMapper.toEntity(cashier)).orElseThrow(()-> new Exception("shift not found"));

        List<Refund> refunds = refundRepository.findByCashierIdAndCreatedAtBetween(
                cashier.getId(),
                shiftReport.getShiftStart(),
                shiftReport.getShiftEnd()
        );

        double totalRefunds =refunds.stream()
                        .mapToDouble(refund -> refund.getAmount()!= null ? refund.getAmount():0.0).sum();

        List<Order> orders = orderRepository.findByCashierIdAndCreatedAtBetween( cashier.getId(),
                shiftReport.getShiftStart(),
                shiftReport.getShiftEnd());
        double totalSales = orders.stream().mapToDouble(Order::getTotalAmount).sum();

        int totalOrders = orders.size();
        double netSales =totalSales-totalRefunds;

        shiftReport.setShiftEnd(shiftEndTime);
        shiftReport.setTotalSales(totalSales);
        shiftReport.setTotalOrders(totalOrders);
        shiftReport.setNetSales(netSales);
        shiftReport.setRecentOrders(getRecentOrders(orders));
        shiftReport.setTopSellingProducts(getTopSellingProducts(orders));
        shiftReport.setPaymentSummeries(getPaymentSummaries(orders,totalSales));
        shiftReport.setRefunds(refunds);
        shiftReportRepository.save(shiftReport);

        return ShiftReportMapper.toDto(shiftReport);
    }

    private List<PaymentSummery> getPaymentSummaries(List<Order> orders, double totalSales) {
//
//        [
//        {type:CASH, totalAmount:34432, transactionCount:3, percentage:60%},
//        {type:CARD, totalAmount:12000, transactionCount:4, percentage:25%},
//        {type:UPI, totalAmount:8000, transactionCount:2, percentage:15%}
//]
        Map<PaymentType, List<Order>> grouped = orders.stream()
                .collect(Collectors.groupingBy(Order::getPaymentType));

        List<PaymentSummery> summaries = new ArrayList<>();

        for (Map.Entry<PaymentType, List<Order>> entry : grouped.entrySet()) {

            PaymentType type = entry.getKey();
            List<Order> orderList = entry.getValue();

            double totalAmount = orderList.stream()
                    .mapToDouble(Order::getTotalAmount)
                    .sum();

            PaymentSummery summary = new PaymentSummery();

            summary.setType(type);
            summary.setTotalAmount(totalAmount);
            summary.setTransactionCount(orderList.size());

            double percentage = totalSales == 0 ? 0 : (totalAmount / totalSales) * 100;
            summary.setPresentage(percentage);

            summaries.add(summary);
        }

        return summaries;
    }
    private List<Product> getTopSellingProducts(List<Order> orders) {

        Map<Product, Integer> productSales = new HashMap<>();

        for (Order order : orders) {

            for (OrderItem item : order.getItems()) {

                Product product = item.getProduct();
                int qty = item.getQuantity();

                productSales.put(product,
                        productSales.getOrDefault(product, 0) + qty);
            }
        }

        return productSales.entrySet()
                .stream()
                .sorted(Map.Entry.<Product, Integer>comparingByValue().reversed())
                .limit(5)
                .map(Map.Entry::getKey)
                .toList();
    }

    private List<Order> getRecentOrders(List<Order> orders) {
        return orders.stream()
                .sorted(Comparator.comparing(Order::getCreatedAt).reversed())
                .limit(5)
                .toList();
    }

    @Override
    public ShiftReportDto getShiftReportById(Long id) {

        ShiftReport shiftReport = shiftReportRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Shift report not found"));

        return ShiftReportMapper.toDto(shiftReport);
    }

    @Override
    public List<ShiftReportDto> getAllShiftReport() {

        return shiftReportRepository.findAll()
                .stream()
                .map(ShiftReportMapper::toDto)
                .toList();
    }

    @Override
    public List<ShiftReportDto> getShiftReportByBranchId(Long branchId) {

        return shiftReportRepository.findByBranchId(branchId)
                .stream()
                .map(ShiftReportMapper::toDto)
                .toList();
    }

    @Override
    public List<ShiftReportDto> getShiftReportByCashierId(Long id) {

        return shiftReportRepository.findByCashierId(id)
                .stream()
                .map(ShiftReportMapper::toDto)
                .toList();
    }

    @Override
    public ShiftReportDto getCurrentShiftProgress(Long cashierId) throws UserExceptions {

        UserDto cashier = userService.getCurrentUser();

        ShiftReport shiftReport =
                shiftReportRepository.findTopByCashierAndShiftEndIsNullOrderByShiftStartDesc(UserMapper.toEntity(cashier))
                        .orElseThrow(() -> new RuntimeException("No active shift found"));

        LocalDateTime now =LocalDateTime.now();
        List<Order> orders =orderRepository.findByCashierIdAndCreatedAtBetween(
                cashier.getId(),
                shiftReport.getShiftStart(),
                now
        );

        List<Refund> refunds = refundRepository.findByCashierIdAndCreatedAtBetween(
                cashier.getId(),
                shiftReport.getShiftStart(),
                shiftReport.getShiftEnd()
        );

        double totalRefunds =refunds.stream()
                .mapToDouble(refund -> refund.getAmount()!= null ? refund.getAmount():0.0).sum();

        List<Order> order = orderRepository.findByCashierIdAndCreatedAtBetween( cashier.getId(),
                shiftReport.getShiftStart(),
                shiftReport.getShiftEnd());
        double totalSales = orders.stream().mapToDouble(Order::getTotalAmount).sum();

        int totalOrders = orders.size();
        double netSales =totalSales-totalRefunds;

        shiftReport.setTotalSales(totalSales);
        shiftReport.setTotalOrders(totalOrders);
        shiftReport.setNetSales(netSales);
        shiftReport.setRecentOrders(getRecentOrders(orders));
        shiftReport.setTopSellingProducts(getTopSellingProducts(orders));
        shiftReport.setPaymentSummeries(getPaymentSummaries(orders,totalSales));
        shiftReport.setRefunds(refunds);
        ShiftReport shiftReport1 =shiftReportRepository.save(shiftReport);





        return ShiftReportMapper.toDto(shiftReport1);
    }

    @Override
    public ShiftReportDto getShiftReportByCashierIdAndDate(Long cashierId, LocalDateTime shiftStartTime) {

        User cashier  =userRepository.findById(cashierId).orElseThrow(()-> new RuntimeException("Cashier not found"));
        shiftStartTime =LocalDateTime.now();
        LocalDateTime startOfDay = shiftStartTime.withHour(0).withMinute(0).withSecond(0);
        LocalDateTime endOfDay = shiftStartTime.withHour(23).withMinute(59).withSecond(59);
        ShiftReport shiftReport =
                shiftReportRepository.findByCashierIdAndShiftStartBetween(
                        cashierId,
                        startOfDay,endOfDay

                ).orElseThrow(() -> new RuntimeException("Shift report not found"));

        return ShiftReportMapper.toDto(shiftReport);
    }
}
