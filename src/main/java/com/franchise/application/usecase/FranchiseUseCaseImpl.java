package com.franchise.application.usecase;

import com.franchise.domain.model.Franchise;
import com.franchise.domain.port.in.FranchiseUseCase;
import com.franchise.domain.port.out.FranchiseRepositoryPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class FranchiseUseCaseImpl implements FranchiseUseCase {

    private final FranchiseRepositoryPort franchiseRepositoryPort;

    @Override
    public Mono<Franchise> createFranchise(String name) {
        return franchiseRepositoryPort.save(
                Franchise.builder().name(name).build()
        );
    }

    @Override
    public Mono<Franchise> updateFranchiseName(Long id, String name) {
        return franchiseRepositoryPort.findById(id)
                .flatMap(franchise -> {
                    franchise.setName(name);
                    return franchiseRepositoryPort.save(franchise);
                });
    }
}
