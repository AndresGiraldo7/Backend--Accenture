package com.franchise.infrastructure.adapter.in.web;

import com.franchise.domain.port.in.ProductUseCase;
import com.franchise.infrastructure.adapter.in.web.dto.WebDto;
import com.franchise.infrastructure.adapter.in.web.mapper.WebMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class ProductController {

    private final ProductUseCase productUseCase;

    @PostMapping("/branches/{branchId}/products")
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<WebDto.ApiResponse<WebDto.ProductResponse>> addProduct(
            @PathVariable Long branchId,
            @RequestBody WebDto.ProductRequest request) {
        return productUseCase.addProductToBranch(branchId, request.getName(), request.getStock())
                .map(WebMapper::toResponse)
                .map(data -> WebDto.ApiResponse.success("Product added successfully", data));
    }

    @DeleteMapping("/branches/{branchId}/products/{productId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public Mono<Void> deleteProduct(
            @PathVariable Long branchId,
            @PathVariable Long productId) {
        return productUseCase.deleteProductFromBranch(branchId, productId);
    }

    @PatchMapping("/products/{productId}/stock")
    public Mono<WebDto.ApiResponse<WebDto.ProductResponse>> updateStock(
            @PathVariable Long productId,
            @RequestBody WebDto.StockRequest request) {
        return productUseCase.updateStock(productId, request.getStock())
                .map(WebMapper::toResponse)
                .map(data -> WebDto.ApiResponse.success("Stock updated successfully", data));
    }

    @PatchMapping("/products/{productId}/name")
    public Mono<WebDto.ApiResponse<WebDto.ProductResponse>> updateName(
            @PathVariable Long productId,
            @RequestBody WebDto.NameRequest request) {
        return productUseCase.updateProductName(productId, request.getName())
                .map(WebMapper::toResponse)
                .map(data -> WebDto.ApiResponse.success("Product name updated successfully", data));
    }

    @GetMapping("/franchises/{franchiseId}/products/top-stock")
    public Flux<WebDto.TopStockResponse> getTopStockByBranch(
            @PathVariable Long franchiseId) {
        return productUseCase.getTopStockByBranchForFranchise(franchiseId)
                .map(WebMapper::toResponse);
    }
}