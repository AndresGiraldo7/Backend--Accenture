package com.franchise.infrastructure.adapter.out.persistence.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table("branch")
public class BranchEntity {

    @Id
    private Long id;
    private String name;

    @Column("franchise_id")
    private Long franchiseId;

    private LocalDateTime createdAt;
}