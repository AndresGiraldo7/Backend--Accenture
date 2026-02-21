package com.franchise.domain.port.out;

import com.franchise.domain.model.Branch;
import reactor.core.publisher.Mono;

public interface BranchRepositoryPort {
    Mono<Branch> save(Branch branch);
    Mono<Branch> findById(Long id);
}