package com.franchise.domain.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Franchise {
    private Long id;
    private String name;
}
