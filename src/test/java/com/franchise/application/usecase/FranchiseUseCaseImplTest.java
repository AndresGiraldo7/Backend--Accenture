package com.franchise.application.usecase;

import com.franchise.domain.model.Franchise;
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
@DisplayName("FranchiseUseCase Tests")
class FranchiseUseCaseImplTest {

    @Mock
    private FranchiseRepositoryPort franchiseRepositoryPort;

    @InjectMocks
    private FranchiseUseCaseImpl franchiseUseCase;

    private Franchise franchise;

    @BeforeEach
    void setUp() {
        franchise = Franchise.builder()
                .id(1L)
                .name("McDonald's")
                .build();
    }

    @Test
    @DisplayName("Should create franchise successfully")
    void createFranchise_success() {
        when(franchiseRepositoryPort.save(any(Franchise.class)))
                .thenReturn(Mono.just(franchise));

        StepVerifier.create(franchiseUseCase.createFranchise("McDonald's"))
                .expectNextMatches(result ->
                        result.getId().equals(1L) &&
                        result.getName().equals("McDonald's")
                )
                .verifyComplete();

        verify(franchiseRepositoryPort, times(1)).save(any(Franchise.class));
    }

    @Test
    @DisplayName("Should update franchise name successfully")
    void updateFranchiseName_success() {
        Franchise updated = Franchise.builder().id(1L).name("Burger King").build();

        when(franchiseRepositoryPort.findById(1L)).thenReturn(Mono.just(franchise));
        when(franchiseRepositoryPort.save(any(Franchise.class))).thenReturn(Mono.just(updated));

        StepVerifier.create(franchiseUseCase.updateFranchiseName(1L, "Burger King"))
                .expectNextMatches(result -> result.getName().equals("Burger King"))
                .verifyComplete();

        verify(franchiseRepositoryPort, times(1)).findById(1L);
        verify(franchiseRepositoryPort, times(1)).save(any(Franchise.class));
    }

    @Test
    @DisplayName("Should throw ResourceNotFoundException when franchise not found on update")
    void updateFranchiseName_notFound() {
        when(franchiseRepositoryPort.findById(99L))
                .thenReturn(Mono.error(new ResourceNotFoundException("Franchise not found with id: 99")));

        StepVerifier.create(franchiseUseCase.updateFranchiseName(99L, "New Name"))
                .expectErrorMatches(error ->
                        error instanceof ResourceNotFoundException &&
                        error.getMessage().contains("99")
                )
                .verify();

        verify(franchiseRepositoryPort, never()).save(any());
    }
}