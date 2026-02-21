package com.franchise.infrastructure.adapter.out.persistence.mapper;

import com.franchise.domain.model.Branch;
import com.franchise.domain.model.Franchise;
import com.franchise.domain.model.Product;
import com.franchise.infrastructure.adapter.out.persistence.entity.BranchEntity;
import com.franchise.infrastructure.adapter.out.persistence.entity.FranchiseEntity;
import com.franchise.infrastructure.adapter.out.persistence.entity.ProductEntity;

import java.time.LocalDateTime;

public class PersistenceMapper {

    private PersistenceMapper() {}

    public static FranchiseEntity toEntity(Franchise franchise) {
        return FranchiseEntity.builder()
                .id(franchise.getId())
                .name(franchise.getName())
                .createdAt(franchise.getId() == null ? LocalDateTime.now() : null)
                .build();
    }

    public static Franchise toDomain(FranchiseEntity entity) {
        return Franchise.builder()
                .id(entity.getId())
                .name(entity.getName())
                .build();
    }

    public static BranchEntity toEntity(Branch branch) {
        return BranchEntity.builder()
                .id(branch.getId())
                .name(branch.getName())
                .franchiseId(branch.getFranchiseId())
                .createdAt(branch.getId() == null ? LocalDateTime.now() : null)
                .build();
    }

    public static Branch toDomain(BranchEntity entity) {
        return Branch.builder()
                .id(entity.getId())
                .name(entity.getName())
                .franchiseId(entity.getFranchiseId())
                .build();
    }

    public static ProductEntity toEntity(Product product) {
        return ProductEntity.builder()
                .id(product.getId())
                .name(product.getName())
                .stock(product.getStock())
                .branchId(product.getBranchId())
                .createdAt(product.getId() == null ? LocalDateTime.now() : null)
                .build();
    }

    public static Product toDomain(ProductEntity entity) {
        return Product.builder()
                .id(entity.getId())
                .name(entity.getName())
                .stock(entity.getStock())
                .branchId(entity.getBranchId())
                .build();
    }
}