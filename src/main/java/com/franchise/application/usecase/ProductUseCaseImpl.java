package com.franchise.application.usecase;

import com.franchise.domain.model.Product;
import com.franchise.domain.model.TopStockResult;
import com.franchise.domain.port.in.ProductUseCase;
import com.franchise.domain.port.out.BranchRepositoryPort;
import com.franchise.domain.port.out.FranchiseRepositoryPort;
import com.franchise.domain.port.out.ProductRepositoryPort;
import com.franchise.infrastructure.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class ProductUseCaseImpl implements ProductUseCase {

    private final ProductRepositoryPort productRepositoryPort;
    private final BranchRepositoryPort branchRepositoryPort;
    private final FranchiseRepositoryPort franchiseRepositoryPort;

    @Override
    public Mono<Product> addProductToBranch(Long branchId, String name, Integer stock) {
        return branchRepositoryPort.findById(branchId)
                .switchIfEmpty(Mono.error(new ResourceNotFoundException("Branch not found with id: " + branchId)))
                .flatMap(branch -> productRepositoryPort.save(
                        Product.builder()
                                .name(name)
                                .stock(stock)
                                .branchId(branchId)
                                .build()
                ));
    }

    @Override
    public Mono<Void> deleteProductFromBranch(Long branchId, Long productId) {
        return branchRepositoryPort.findById(branchId)
                .switchIfEmpty(Mono.error(new ResourceNotFoundException("Branch not found with id: " + branchId)))
                .flatMap(branch -> productRepositoryPort.findById(productId)
                        .switchIfEmpty(Mono.error(new ResourceNotFoundException("Product not found with id: " + productId)))
                        .flatMap(productRepositoryPort::delete)
                );
    }

    @Override
    public Mono<Product> updateStock(Long productId, Integer stock) {
        return productRepositoryPort.findById(productId)
                .switchIfEmpty(Mono.error(new ResourceNotFoundException("Product not found with id: " + productId)))
                .flatMap(product -> {
                    product.setStock(stock);
                    return productRepositoryPort.save(product);
                });
    }

    @Override
    public Mono<Product> updateProductName(Long productId, String name) {
        return productRepositoryPort.findById(productId)
                .switchIfEmpty(Mono.error(new ResourceNotFoundException("Product not found with id: " + productId)))
                .flatMap(product -> {
                    product.setName(name);
                    return productRepositoryPort.save(product);
                });
    }

    @Override
    public Flux<TopStockResult> getTopStockByBranchForFranchise(Long franchiseId) {
        return franchiseRepositoryPort.findById(franchiseId)
                .switchIfEmpty(Mono.error(new ResourceNotFoundException("Franchise not found with id: " + franchiseId)))
                .flatMapMany(franchise -> productRepositoryPort.findTopStockByBranchForFranchise(franchiseId));
    }
}