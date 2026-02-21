package com.franchise.infrastructure.adapter.in.web.mapper;

import com.franchise.domain.model.Branch;
import com.franchise.domain.model.Franchise;
import com.franchise.domain.model.Product;
import com.franchise.domain.model.TopStockResult;
import com.franchise.infrastructure.adapter.in.web.dto.WebDto;

public class WebMapper {

    private WebMapper() {}

    public static WebDto.FranchiseResponse toResponse(Franchise franchise) {
        return WebDto.FranchiseResponse.builder()
                .id(franchise.getId())
                .name(franchise.getName())
                .build();
    }

    public static WebDto.BranchResponse toResponse(Branch branch) {
        return WebDto.BranchResponse.builder()
                .id(branch.getId())
                .name(branch.getName())
                .franchiseId(branch.getFranchiseId())
                .build();
    }

    public static WebDto.ProductResponse toResponse(Product product) {
        return WebDto.ProductResponse.builder()
                .id(product.getId())
                .name(product.getName())
                .stock(product.getStock())
                .branchId(product.getBranchId())
                .build();
    }

    public static WebDto.TopStockResponse toResponse(TopStockResult result) {
        return WebDto.TopStockResponse.builder()
                .productId(result.getProductId())
                .productName(result.getProductName())
                .stock(result.getStock())
                .branchId(result.getBranchId())
                .branchName(result.getBranchName())
                .build();
    }
}