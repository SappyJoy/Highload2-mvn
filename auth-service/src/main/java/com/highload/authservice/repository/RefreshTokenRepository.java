package com.highload.authservice.repository;

import com.highload.authservice.model.RefreshToken;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;

@Repository
public interface RefreshTokenRepository extends ReactiveCrudRepository<RefreshToken, Long> {

    @Query("SELECT * FROM refreshToken WHERE token = :token")
    Flux<RefreshToken> findByToken(String token);
}
