package com.example.order_management.repository;

import com.example.order_management.model.Client;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@RepositoryRestResource(path = "clients")
public interface ClientRepository extends JpaRepository<Client, Long> {
    Optional<Client> findByEmail(String email);
}

