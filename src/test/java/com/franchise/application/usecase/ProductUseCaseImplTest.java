package com.franchise.application.usecase;

import com.franchise.domain.model.Branch;
import com.franchise.domain.model.Franchise;
import com.franchise.domain.model.Product;
import com.franchise.domain.model.TopStockResult;
import com.franchise.domain.port.out.BranchRepositoryPort;
import com.franchise.domain.port.out.FranchiseRepositoryPort;
import com.franchise.domain.port.out.ProductRepositoryPort;
import com.franchise.infrastructure.exception.ResourceNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("ProductUseCase Tests")
class ProductUseCaseImplTest {

    @Mock
    private ProductRepositoryPort productRepositoryPort;

    @Mock
    private BranchRepositoryPort branchRepositoryPort;

    @Mock
    private FranchiseRepositoryPort franchiseRepositoryPort;

    @InjectMocks
    private ProductUseCaseImpl productUseCase;

    private Franchise franchise;
    private Branch branch;
    private Product product;

    @BeforeEach
    void setUp() {
        franchise = Franchise.builder().id(1L).name("McDonald's").build();
        branch = Branch.builder().id(1L).name("Sucursal Norte").franchiseId(1L).build();
        product = Product.builder().id(1L).name("Big Mac").stock(100).branchId(1L).build();
    }

    @Test
    @DisplayName("Should add product to branch successfully")
    void addProductToBranch_success() {
        when(branchRepositoryPort.findById(1L)).thenReturn(Mono.just(branch));
        when(productRepositoryPort.save(any(Product.class))).thenReturn(Mono.just(product));

        StepVerifier.create(productUseCase.addProductToBranch(1L, "Big Mac", 100))
                .expectNextMatches(result ->
                        result.getId().equals(1L) &&
                        result.getName().equals("Big Mac") &&
                        result.getStock().equals(100) &&
                        result.getBranchId().equals(1L)
                )
                .verifyComplete();

        verify(branchRepositoryPort, times(1)).findById(1L);
        verify(productRepositoryPort, times(1)).save(any(Product.class));
    }

    @Test
    @DisplayName("Should throw ResourceNotFoundException when branch not found on add product")
    void addProductToBranch_branchNotFound() {
        when(branchRepositoryPort.findById(99L))
                .thenReturn(Mono.error(new ResourceNotFoundException("Branch not found with id: 99")));

        StepVerifier.create(productUseCase.addProductToBranch(99L, "Big Mac", 100))
                .expectErrorMatches(error ->
                        error instanceof ResourceNotFoundException &&
                        error.getMessage().contains("99")
                )
                .verify();

        verify(productRepositoryPort, never()).save(any());
    }

    @Test
    @DisplayName("Should delete product from branch successfully")
    void deleteProductFromBranch_success() {
        when(branchRepositoryPort.findById(1L)).thenReturn(Mono.just(branch));
        when(productRepositoryPort.findById(1L)).thenReturn(Mono.just(product));
        when(productRepositoryPort.delete(product)).thenReturn(Mono.empty());

        StepVerifier.create(productUseCase.deleteProductFromBranch(1L, 1L))
                .verifyComplete();

        verify(productRepositoryPort, times(1)).delete(product);
    }

    @Test
    @DisplayName("Should throw ResourceNotFoundException when product not found on delete")
    void deleteProductFromBranch_productNotFound() {
        when(branchRepositoryPort.findById(1L)).thenReturn(Mono.just(branch));
        when(productRepositoryPort.findById(99L))
                .thenReturn(Mono.error(new ResourceNotFoundException("Product not found with id: 99")));

        StepVerifier.create(productUseCase.deleteProductFromBranch(1L, 99L))
                .expectErrorMatches(error ->
                        error instanceof ResourceNotFoundException &&
                        error.getMessage().contains("99")
                )
                .verify();

        verify(productRepositoryPort, never()).delete(any());
    }

    @Test
    @DisplayName("Should update stock successfully")
    void updateStock_success() {
        Product updated = Product.builder().id(1L).name("Big Mac").stock(250).branchId(1L).build();

        when(productRepositoryPort.findById(1L)).thenReturn(Mono.just(product));
        when(productRepositoryPort.save(any(Product.class))).thenReturn(Mono.just(updated));

        StepVerifier.create(productUseCase.updateStock(1L, 250))
                .expectNextMatches(result -> result.getStock().equals(250))
                .verifyComplete();

        verify(productRepositoryPort, times(1)).findById(1L);
        verify(productRepositoryPort, times(1)).save(any(Product.class));
    }

    @Test
    @DisplayName("Should throw ResourceNotFoundException when product not found on update stock")
    void updateStock_notFound() {
        when(productRepositoryPort.findById(99L))
                .thenReturn(Mono.error(new ResourceNotFoundException("Product not found with id: 99")));

        StepVerifier.create(productUseCase.updateStock(99L, 250))
                .expectErrorMatches(error ->
                        error instanceof ResourceNotFoundException &&
                        error.getMessage().contains("99")
                )
                .verify();

        verify(productRepositoryPort, never()).save(any());
    }

    @Test
    @DisplayName("Should update product name successfully")
    void updateProductName_success() {
        Product updated = Product.builder().id(1L).name("McPollo").stock(100).branchId(1L).build();

        when(productRepositoryPort.findById(1L)).thenReturn(Mono.just(product));
        when(productRepositoryPort.save(any(Product.class))).thenReturn(Mono.just(updated));

        StepVerifier.create(productUseCase.updateProductName(1L, "McPollo"))
                .expectNextMatches(result -> result.getName().equals("McPollo"))
                .verifyComplete();
    }

    @Test
    @DisplayName("Should return top stock products per branch for a franchise")
    void getTopStockByBranchForFranchise_success() {
        TopStockResult topResult1 = TopStockResult.builder()
                .productId(1L).productName("Big Mac").stock(100)
                .branchId(1L).branchName("Sucursal Norte")
                .build();
        TopStockResult topResult2 = TopStockResult.builder()
                .productId(3L).productName("McPollo").stock(80)
                .branchId(2L).branchName("Sucursal Sur")
                .build();

        when(franchiseRepositoryPort.findById(1L)).thenReturn(Mono.just(franchise));
        when(productRepositoryPort.findTopStockByBranchForFranchise(1L))
                .thenReturn(Flux.just(topResult1, topResult2));

        StepVerifier.create(productUseCase.getTopStockByBranchForFranchise(1L))
                .expectNextMatches(r -> r.getBranchName().equals("Sucursal Norte") && r.getStock().equals(100))
                .expectNextMatches(r -> r.getBranchName().equals("Sucursal Sur") && r.getStock().equals(80))
                .verifyComplete();

        verify(franchiseRepositoryPort, times(1)).findById(1L);
        verify(productRepositoryPort, times(1)).findTopStockByBranchForFranchise(1L);
    }

    @Test
    @DisplayName("Should throw ResourceNotFoundException when franchise not found on top stock query")
    void getTopStockByBranchForFranchise_franchiseNotFound() {
        when(franchiseRepositoryPort.findById(99L))
                .thenReturn(Mono.error(new ResourceNotFoundException("Franchise not found with id: 99")));

        StepVerifier.create(productUseCase.getTopStockByBranchForFranchise(99L))
                .expectErrorMatches(error ->
                        error instanceof ResourceNotFoundException &&
                        error.getMessage().contains("99")
                )
                .verify();

        verify(productRepositoryPort, never()).findTopStockByBranchForFranchise(any());
    }
}