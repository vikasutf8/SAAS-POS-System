package com.pm.saaspossystem.services.implementions;

import com.pm.saaspossystem.mapper.BranchMapper;
import com.pm.saaspossystem.mapper.OrderMapper;
import com.pm.saaspossystem.mapper.RefundMapper;
import com.pm.saaspossystem.model.Branch;
import com.pm.saaspossystem.model.Order;
import com.pm.saaspossystem.model.Refund;
import com.pm.saaspossystem.payload.dto.RefundDto;
import com.pm.saaspossystem.payload.dto.UserDto;
import com.pm.saaspossystem.repository.OrderRepository;
import com.pm.saaspossystem.repository.RefundRepository;
import com.pm.saaspossystem.services.RefundService;
import com.pm.saaspossystem.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RefundServiceImplmention implements RefundService {
    private final RefundRepository refundRepository;
    private final UserService userService;
    private final OrderRepository orderRepository;

    @Override
    public RefundDto createRefund(RefundDto refundDto) throws Exception {
        UserDto cashier =userService.getCurrentUser();

        Order order = orderRepository.findById(refundDto.getOrderId()).orElseThrow(()->new Exception("order not found"));

        Branch branch =order.getBranch();

        refundDto.setBranch(BranchMapper.toDto(branch));
        refundDto.setBranchId(branch.getId());

        refundDto.setOrder(OrderMapper.toDto(order));
        refundDto.setOrderId(order.getId());

        refundDto.setCashier(cashier);
        refundDto.setCashierId(cashier.getId());


        Refund refund = RefundMapper.toEntity(refundDto);

        Refund savedRefund = refundRepository.save(refund);

        return RefundMapper.toDto(savedRefund);
    }

    @Override
    public List<RefundDto> getAllRefunds() {

        return refundRepository.findAll()
                .stream()
                .map(RefundMapper::toDto)
                .toList();
    }

    @Override
    public List<RefundDto>  getRefundByCashier(Long cashierId) {

        return refundRepository.findByCashierId(cashierId)
                .stream()
                .map(RefundMapper::toDto)
                .collect(Collectors.toList());


    }

    @Override
    public List<RefundDto> getRefundByShiftReport(Long shiftReportId) {

        return  refundRepository.findByShiftReportId(shiftReportId)
                .stream()
                .map(RefundMapper::toDto)
                .collect(Collectors.toList());

    }

    @Override
    public List<RefundDto> getRefundByCashierAndDateRange(
            Long cashierId,
            LocalDateTime startDate,
            LocalDateTime endDate
    ) {

        return refundRepository
                .findByCashierIdAndCreatedAtBetween(cashierId, startDate, endDate)
                .stream()
                .map(RefundMapper::toDto)
                .toList();
    }

    @Override
    public List<RefundDto> getRefundByBranch(Long branchId) {

        return refundRepository
                .findByBranchId(branchId)
                .stream()
                .map(RefundMapper::toDto)
                .toList();
    }

    @Override
    public RefundDto getRefundById(Long refundId) {

        Refund refund = refundRepository.findById(refundId)
                .orElseThrow(() -> new RuntimeException("Refund not found"));

        return RefundMapper.toDto(refund);
    }

    @Override
    public void deleteRefund(Long refundId) {

        Refund refund = refundRepository.findById(refundId)
                .orElseThrow(() -> new RuntimeException("Refund not found"));

        refundRepository.delete(refund);
    }
}
