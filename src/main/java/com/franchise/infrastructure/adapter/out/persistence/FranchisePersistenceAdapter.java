package com.franchise.infrastructure.adapter.out.persistence;

import com.franchise.domain.model.Franchise;
import com.franchise.domain.port.out.FranchiseRepositoryPort;
import com.franchise.infrastructure.adapter.out.persistence.mapper.PersistenceMapper;
import com.franchise.infrastructure.adapter.out.persistence.repository.FranchiseR2dbcRepository;
import com.franchise.infrastructure.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class FranchisePersistenceAdapter implements FranchiseRepositoryPort {

    private final FranchiseR2dbcRepository repository;

    @Override
    public Mono<Franchise> save(Franchise franchise) {
        return repository.save(PersistenceMapper.toEntity(franchise))
                .map(PersistenceMapper::toDomain);
    }

    @Override
    public Mono<Franchise> findById(Long id) {
        return repository.findById(id)
                .switchIfEmpty(Mono.error(new ResourceNotFoundException("Franchise not found with id: " + id)))
                .map(PersistenceMapper::toDomain);
    }
}