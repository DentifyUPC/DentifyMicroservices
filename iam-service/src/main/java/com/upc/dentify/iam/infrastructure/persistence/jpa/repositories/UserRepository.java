package com.upc.dentify.iam.infrastructure.persistence.jpa.repositories;

import com.upc.dentify.iam.domain.model.aggregates.User;
import com.upc.dentify.iam.domain.model.valueobjects.Username;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(Username username);

}