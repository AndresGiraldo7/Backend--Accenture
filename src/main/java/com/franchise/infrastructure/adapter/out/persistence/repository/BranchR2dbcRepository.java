package com.franchise.infrastructure.adapter.out.persistence.repository;

import com.franchise.infrastructure.adapter.out.persistence.entity.BranchEntity;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;

public interface BranchR2dbcRepository extends ReactiveCrudRepository<BranchEntity, Long> {
}