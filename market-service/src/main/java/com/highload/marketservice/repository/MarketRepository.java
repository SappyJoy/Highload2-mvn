package com.highload.marketservice.repository;

import com.highload.feign.model.Market;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;


@Repository
public interface MarketRepository extends JpaRepository<Market, UUID> {
    Page<Market> findAll(Pageable pageable);

    void deleteMarketByMarketId(UUID marketId);

    Optional<Market> findById(UUID uuid);
}
