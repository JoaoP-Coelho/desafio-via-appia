package com.incidentmanager.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.incidentmanager.entity.User;

import java.util.Optional;
import java.util.UUID;

public interface UserRepository extends JpaRepository<User, UUID> {

    Optional<User> findByLogin(String login);

}