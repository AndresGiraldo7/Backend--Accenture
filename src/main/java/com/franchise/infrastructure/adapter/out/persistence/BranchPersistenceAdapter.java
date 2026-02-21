package com.franchise.infrastructure.adapter.out.persistence;

import com.franchise.domain.model.Branch;
import com.franchise.domain.port.out.BranchRepositoryPort;
import com.franchise.infrastructure.adapter.out.persistence.mapper.PersistenceMapper;
import com.franchise.infrastructure.adapter.out.persistence.repository.BranchR2dbcRepository;
import com.franchise.infrastructure.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class BranchPersistenceAdapter implements BranchRepositoryPort {

    private final BranchR2dbcRepository repository;

    @Override
    public Mono<Branch> save(Branch branch) {
        return repository.save(PersistenceMapper.toEntity(branch))
                .map(PersistenceMapper::toDomain);
    }

    @Override
    public Mono<Branch> findById(Long id) {
        return repository.findById(id)
                .switchIfEmpty(Mono.error(new ResourceNotFoundException("Branch not found with id: " + id)))
                .map(PersistenceMapper::toDomain);
    }
}