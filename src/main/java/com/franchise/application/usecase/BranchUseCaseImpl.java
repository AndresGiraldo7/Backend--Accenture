package com.franchise.application.usecase;

import com.franchise.domain.model.Branch;
import com.franchise.domain.port.in.BranchUseCase;
import com.franchise.domain.port.out.BranchRepositoryPort;
import com.franchise.domain.port.out.FranchiseRepositoryPort;
import com.franchise.infrastructure.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class BranchUseCaseImpl implements BranchUseCase {

    private final BranchRepositoryPort branchRepositoryPort;
    private final FranchiseRepositoryPort franchiseRepositoryPort;

    @Override
    public Mono<Branch> addBranchToFranchise(Long franchiseId, String name) {
        return franchiseRepositoryPort.findById(franchiseId)
                .switchIfEmpty(Mono.error(new ResourceNotFoundException("Franchise not found with id: " + franchiseId)))
                .flatMap(franchise -> branchRepositoryPort.save(
                        Branch.builder()
                                .name(name)
                                .franchiseId(franchiseId)
                                .build()
                ));
    }

    @Override
    public Mono<Branch> updateBranchName(Long branchId, String name) {
        return branchRepositoryPort.findById(branchId)
                .switchIfEmpty(Mono.error(new ResourceNotFoundException("Branch not found with id: " + branchId)))
                .flatMap(branch -> {
                    branch.setName(name);
                    return branchRepositoryPort.save(branch);
                });
    }
}
