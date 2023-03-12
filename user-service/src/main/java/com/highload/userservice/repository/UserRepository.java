package com.highload.userservice.repository;

import com.highload.feign.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;


@Repository
public interface UserRepository extends JpaRepository<User, UUID> {
    Page<User> findAll(Pageable pageable);

    Optional<User> findUserByUserId(UUID id);

    void deleteUserByUserId(UUID id);

    Optional<User> findByUsername(String username);
}
