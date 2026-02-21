package com.franchise.infrastructure.adapter.out.persistence;

import com.franchise.domain.model.Product;
import com.franchise.domain.model.TopStockResult;
import com.franchise.domain.port.out.BranchRepositoryPort;
import com.franchise.domain.port.out.ProductRepositoryPort;
import com.franchise.infrastructure.adapter.out.persistence.mapper.PersistenceMapper;
import com.franchise.infrastructure.adapter.out.persistence.repository.ProductR2dbcRepository;
import com.franchise.infrastructure.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class ProductPersistenceAdapter implements ProductRepositoryPort {

    private final ProductR2dbcRepository repository;
    private final BranchRepositoryPort branchRepositoryPort;

    @Override
    public Mono<Product> save(Product product) {
        return repository.save(PersistenceMapper.toEntity(product))
                .map(PersistenceMapper::toDomain);
    }

    @Override
    public Mono<Product> findById(Long id) {
        return repository.findById(id)
                .switchIfEmpty(Mono.error(new ResourceNotFoundException("Product not found with id: " + id)))
                .map(PersistenceMapper::toDomain);
    }

    @Override
    public Mono<Void> delete(Product product) {
        return repository.delete(PersistenceMapper.toEntity(product));
    }

    @Override
    public Flux<TopStockResult> findTopStockByBranchForFranchise(Long franchiseId) {
        return repository.findTopStockByBranchForFranchise(franchiseId)
                .flatMap(productEntity -> branchRepositoryPort.findById(productEntity.getBranchId())
                        .map(branch -> TopStockResult.builder()
                                .productId(productEntity.getId())
                                .productName(productEntity.getName())
                                .stock(productEntity.getStock())
                                .branchId(branch.getId())
                                .branchName(branch.getName())
                                .build()
                        )
                );
    }
}