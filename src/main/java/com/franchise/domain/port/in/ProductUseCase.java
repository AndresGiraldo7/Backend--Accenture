package com.franchise.domain.port.in;

import com.franchise.domain.model.Product;
import com.franchise.domain.model.TopStockResult;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface ProductUseCase {
    Mono<Product> addProductToBranch(Long branchId, String name, Integer stock);
    Mono<Void> deleteProductFromBranch(Long branchId, Long productId);
    Mono<Product> updateStock(Long productId, Integer stock);
    Mono<Product> updateProductName(Long productId, String name);
    Flux<TopStockResult> getTopStockByBranchForFranchise(Long franchiseId);
}
