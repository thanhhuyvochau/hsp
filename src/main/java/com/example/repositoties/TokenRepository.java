package com.example.repositoties;

import com.example.entities.Token;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository("tokenRepository")
public interface TokenRepository extends JpaRepository<Token, Long> {
    Token findByToken(String token);
    Token findByUserEmail(String email);
}
