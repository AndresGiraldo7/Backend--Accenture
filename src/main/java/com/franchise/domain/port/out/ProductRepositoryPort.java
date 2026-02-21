package com.franchise.domain.port.out;

import com.franchise.domain.model.Product;
import com.franchise.domain.model.TopStockResult;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface ProductRepositoryPort {
    Mono<Product> save(Product product);
    Mono<Product> findById(Long id);
    Mono<Void> delete(Product product);
    Flux<TopStockResult> findTopStockByBranchForFranchise(Long franchiseId);
}