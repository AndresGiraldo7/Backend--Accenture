package com.franchise.domain.port.in;

import com.franchise.domain.model.Franchise;
import reactor.core.publisher.Mono;

public interface FranchiseUseCase {
    Mono<Franchise> createFranchise(String name);
    Mono<Franchise> updateFranchiseName(Long id, String name);
}