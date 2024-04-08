package com.project.workflow.repository;

import com.project.workflow.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User,Long> {

    Optional<User> findByEmail(String email);

    @Query("select u from User u where u.email=:email")
    User findUserByEmail(String email);

    @Modifying
    @Transactional
    @Query("UPDATE User u SET u.userRatings = :rating WHERE u.userId = :userId")
    void updateRatings(@Param("rating") double rating, @Param("userId") Long userId);

    List<User> findByEmailContaining(String text);
}
