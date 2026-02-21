package com.franchise.infrastructure.adapter.in.web.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

public class WebDto {

    @Data @NoArgsConstructor @AllArgsConstructor
    public static class NameRequest {
        private String name;
    }

    @Data @NoArgsConstructor @AllArgsConstructor
    public static class StockRequest {
        private Integer stock;
    }

    @Data @NoArgsConstructor @AllArgsConstructor
    public static class ProductRequest {
        private String name;
        private Integer stock;
    }

    @Data @Builder @NoArgsConstructor @AllArgsConstructor
    public static class FranchiseResponse {
        private Long id;
        private String name;
    }

    @Data @Builder @NoArgsConstructor @AllArgsConstructor
    public static class BranchResponse {
        private Long id;
        private String name;
        private Long franchiseId;
    }

    @Data @Builder @NoArgsConstructor @AllArgsConstructor
    public static class ProductResponse {
        private Long id;
        private String name;
        private Integer stock;
        private Long branchId;
    }

    @Data @Builder @NoArgsConstructor @AllArgsConstructor
    public static class TopStockResponse {
        private Long productId;
        private String productName;
        private Integer stock;
        private Long branchId;
        private String branchName;
    }

    @Data @Builder @NoArgsConstructor @AllArgsConstructor
    public static class ApiResponse<T> {
        private String message;
        private T data;

        public static <T> ApiResponse<T> success(String message, T data) {
            return ApiResponse.<T>builder().message(message).data(data).build();
        }
    }
}