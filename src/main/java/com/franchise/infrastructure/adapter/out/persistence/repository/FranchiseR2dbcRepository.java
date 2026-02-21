package com.franchise.infrastructure.adapter.out.persistence.repository;

import com.franchise.infrastructure.adapter.out.persistence.entity.FranchiseEntity;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;

public interface FranchiseR2dbcRepository extends ReactiveCrudRepository<FranchiseEntity, Long> {
}