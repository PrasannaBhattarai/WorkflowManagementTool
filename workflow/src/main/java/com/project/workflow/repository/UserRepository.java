package com.project.workflow.repository;

import com.project.workflow.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User,Long> {

    Optional<User> findByEmail(String email);

    @Query("select u from User u where u.email=:email")
    User findUserByEmail(String email);

    List<User> findByEmailContaining(String text);
}
