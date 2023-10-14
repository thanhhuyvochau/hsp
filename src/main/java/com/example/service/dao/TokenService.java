package com.example.service.dao;

import com.example.entities.Token;
import com.example.entities.User;

public interface TokenService {
    Token createToken(User user);
    Token findTokenByValue(String tokenValue);
    void deleteToken(Token token);
}
