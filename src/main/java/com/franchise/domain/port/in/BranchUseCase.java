package com.franchise.domain.port.in;

import com.franchise.domain.model.Branch;
import reactor.core.publisher.Mono;

public interface BranchUseCase {
    Mono<Branch> addBranchToFranchise(Long franchiseId, String name);
    Mono<Branch> updateBranchName(Long branchId, String name);
}