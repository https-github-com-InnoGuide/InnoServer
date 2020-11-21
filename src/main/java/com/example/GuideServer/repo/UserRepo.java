package com.example.GuideServer.repo;

import com.example.GuideServer.Entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

public interface UserRepo extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);

    @Modifying
    @Transactional
    @Query("update User u set u.password = :password where u.id = :id")
    void updatePassword(@Param(value = "id") long id, @Param(value = "password") String password);

}
