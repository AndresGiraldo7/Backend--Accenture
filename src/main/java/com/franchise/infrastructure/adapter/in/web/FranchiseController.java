package com.franchise.infrastructure.adapter.in.web;

import com.franchise.domain.port.in.FranchiseUseCase;
import com.franchise.infrastructure.adapter.in.web.dto.WebDto;
import com.franchise.infrastructure.adapter.in.web.mapper.WebMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/franchises")
@RequiredArgsConstructor
public class FranchiseController {

    private final FranchiseUseCase franchiseUseCase;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<WebDto.ApiResponse<WebDto.FranchiseResponse>> create(
            @RequestBody WebDto.NameRequest request) {
        return franchiseUseCase.createFranchise(request.getName())
                .map(WebMapper::toResponse)
                .map(data -> WebDto.ApiResponse.success("Franchise created successfully", data));
    }

    @PatchMapping("/{id}/name")
    public Mono<WebDto.ApiResponse<WebDto.FranchiseResponse>> updateName(
            @PathVariable Long id,
            @RequestBody WebDto.NameRequest request) {
        return franchiseUseCase.updateFranchiseName(id, request.getName())
                .map(WebMapper::toResponse)
                .map(data -> WebDto.ApiResponse.success("Franchise name updated successfully", data));
    }
}