package com.franchise.application.usecase;

import com.franchise.domain.model.Branch;
import com.franchise.domain.model.Franchise;
import com.franchise.domain.port.out.BranchRepositoryPort;
import com.franchise.domain.port.out.FranchiseRepositoryPort;
import com.franchise.infrastructure.exception.ResourceNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("BranchUseCase Tests")
class BranchUseCaseImplTest {

    @Mock
    private BranchRepositoryPort branchRepositoryPort;

    @Mock
    private FranchiseRepositoryPort franchiseRepositoryPort;

    @InjectMocks
    private BranchUseCaseImpl branchUseCase;

    private Franchise franchise;
    private Branch branch;

    @BeforeEach
    void setUp() {
        franchise = Franchise.builder().id(1L).name("McDonald's").build();
        branch = Branch.builder().id(1L).name("Sucursal Norte").franchiseId(1L).build();
    }

    @Test
    @DisplayName("Should add branch to franchise successfully")
    void addBranchToFranchise_success() {
        when(franchiseRepositoryPort.findById(1L)).thenReturn(Mono.just(franchise));
        when(branchRepositoryPort.save(any(Branch.class))).thenReturn(Mono.just(branch));

        StepVerifier.create(branchUseCase.addBranchToFranchise(1L, "Sucursal Norte"))
                .expectNextMatches(result ->
                        result.getId().equals(1L) &&
                        result.getName().equals("Sucursal Norte") &&
                        result.getFranchiseId().equals(1L)
                )
                .verifyComplete();

        verify(franchiseRepositoryPort, times(1)).findById(1L);
        verify(branchRepositoryPort, times(1)).save(any(Branch.class));
    }

    @Test
    @DisplayName("Should throw ResourceNotFoundException when franchise not found on add branch")
    void addBranchToFranchise_franchiseNotFound() {
        when(franchiseRepositoryPort.findById(99L))
                .thenReturn(Mono.error(new ResourceNotFoundException("Franchise not found with id: 99")));

        StepVerifier.create(branchUseCase.addBranchToFranchise(99L, "Sucursal Norte"))
                .expectErrorMatches(error ->
                        error instanceof ResourceNotFoundException &&
                        error.getMessage().contains("99")
                )
                .verify();

        verify(branchRepositoryPort, never()).save(any());
    }

    @Test
    @DisplayName("Should update branch name successfully")
    void updateBranchName_success() {
        Branch updated = Branch.builder().id(1L).name("Sucursal Sur").franchiseId(1L).build();

        when(branchRepositoryPort.findById(1L)).thenReturn(Mono.just(branch));
        when(branchRepositoryPort.save(any(Branch.class))).thenReturn(Mono.just(updated));

        StepVerifier.create(branchUseCase.updateBranchName(1L, "Sucursal Sur"))
                .expectNextMatches(result -> result.getName().equals("Sucursal Sur"))
                .verifyComplete();

        verify(branchRepositoryPort, times(1)).findById(1L);
        verify(branchRepositoryPort, times(1)).save(any(Branch.class));
    }

    @Test
    @DisplayName("Should throw ResourceNotFoundException when branch not found on update")
    void updateBranchName_notFound() {
        when(branchRepositoryPort.findById(99L))
                .thenReturn(Mono.error(new ResourceNotFoundException("Branch not found with id: 99")));

        StepVerifier.create(branchUseCase.updateBranchName(99L, "New Name"))
                .expectErrorMatches(error ->
                        error instanceof ResourceNotFoundException &&
                        error.getMessage().contains("99")
                )
                .verify();

        verify(branchRepositoryPort, never()).save(any());
    }
}