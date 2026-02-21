package com.franchise.domain.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class TopStockResult {
    private Long productId;
    private String productName;
    private Integer stock;
    private Long branchId;
    private String branchName;
}