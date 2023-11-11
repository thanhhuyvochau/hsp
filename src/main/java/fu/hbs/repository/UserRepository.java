/*
 * Copyright (C) 2023, FPT University
 * SEP490 - SEP490_G77
 * HBS
 * Hotel Booking System
 *
 * Record of change:
 * DATE          Version    Author           DESCRIPTION
 * 04/10/2023    1.0        HieuLBM          First Deploy
 *
 *
 */
package fu.hbs.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.stereotype.Repository;

import fu.hbs.entities.User;

@Repository("userRepository")
@EnableJpaRepositories
public interface UserRepository extends JpaRepository<User, Long> {
    User findByEmail(String email);

    User getUserByEmail(String email);

    Optional<User> findById(Long id);

    Optional<User> findByPhone(String phone);

    User findByUserId(Long id);
}
