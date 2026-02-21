package com.franchise.infrastructure.adapter.out.persistence.repository;

import com.franchise.infrastructure.adapter.out.persistence.entity.ProductEntity;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;

public interface ProductR2dbcRepository extends ReactiveCrudRepository<ProductEntity, Long> {

    @Query("""
        SELECT p.* FROM product p
        INNER JOIN (
            SELECT branch_id, MAX(stock) AS max_stock
            FROM product
            WHERE branch_id IN (SELECT id FROM branch WHERE franchise_id = :franchiseId)
            GROUP BY branch_id
        ) max_p ON p.branch_id = max_p.branch_id AND p.stock = max_p.max_stock
        WHERE p.branch_id IN (SELECT id FROM branch WHERE franchise_id = :franchiseId)
    """)
    Flux<ProductEntity> findTopStockByBranchForFranchise(Long franchiseId);
}
