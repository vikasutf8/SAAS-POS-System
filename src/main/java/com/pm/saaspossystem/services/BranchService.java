package com.pm.saaspossystem.services;

import com.pm.saaspossystem.exceptions.UserExceptions;
import com.pm.saaspossystem.payload.dto.BranchDto;

import java.util.List;

public interface BranchService {

    BranchDto createBranch(BranchDto branchDto) throws UserExceptions;
    BranchDto updateBranch(Long id, BranchDto branchDto) throws UserExceptions;
    void deleteBranch(Long id);
List<BranchDto> getAllBranchByStoreId(Long storeID);
BranchDto getBranchById(Long id);
}
