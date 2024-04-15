package com.project.workflow.repository;

import com.project.workflow.models.UnregisteredUser;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UnregisteredUserRepository extends JpaRepository<UnregisteredUser, Long> {
}