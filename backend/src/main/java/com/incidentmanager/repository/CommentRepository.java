package com.incidentmanager.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.incidentmanager.entity.Comment;

import java.util.List;
import java.util.UUID;

public interface CommentRepository extends JpaRepository<Comment, UUID> {

    List<Comment> findByIncidentId(UUID incidentId);

}