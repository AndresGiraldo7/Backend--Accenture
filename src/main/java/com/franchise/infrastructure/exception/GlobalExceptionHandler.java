package com.franchise.infrastructure.exception;

import com.franchise.infrastructure.adapter.in.web.dto.WebDto;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import reactor.core.publisher.Mono;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ResourceNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public Mono<WebDto.ApiResponse<Void>> handleNotFound(ResourceNotFoundException ex) {
        return Mono.just(WebDto.ApiResponse.<Void>builder()
                .message(ex.getMessage())
                .build());
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public Mono<WebDto.ApiResponse<Void>> handleGeneral(Exception ex) {
        return Mono.just(WebDto.ApiResponse.<Void>builder()
                .message("Internal server error: " + ex.getMessage())
                .build());
    }
}