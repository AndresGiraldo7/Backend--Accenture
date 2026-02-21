package com.franchise.infrastructure.adapter.in.web;

import com.franchise.domain.port.in.BranchUseCase;
import com.franchise.infrastructure.adapter.in.web.dto.WebDto;
import com.franchise.infrastructure.adapter.in.web.mapper.WebMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class BranchController {

    private final BranchUseCase branchUseCase;

    @PostMapping("/franchises/{franchiseId}/branches")
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<WebDto.ApiResponse<WebDto.BranchResponse>> addBranch(
            @PathVariable Long franchiseId,
            @RequestBody WebDto.NameRequest request) {
        return branchUseCase.addBranchToFranchise(franchiseId, request.getName())
                .map(WebMapper::toResponse)
                .map(data -> WebDto.ApiResponse.success("Branch added successfully", data));
    }

    @PatchMapping("/branches/{branchId}/name")
    public Mono<WebDto.ApiResponse<WebDto.BranchResponse>> updateName(
            @PathVariable Long branchId,
            @RequestBody WebDto.NameRequest request) {
        return branchUseCase.updateBranchName(branchId, request.getName())
                .map(WebMapper::toResponse)
                .map(data -> WebDto.ApiResponse.success("Branch name updated successfully", data));
    }
}