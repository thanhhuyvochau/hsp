package com.example.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "Token")
public class Token {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "user_id")
    private User user;

    private String token;

    @Temporal(TemporalType.TIMESTAMP)
    private Date expirationDate;
    public boolean isExpired() {
        // Kiểm tra xem thời gian hiện tại có lớn hơn thời gian hết hạn không
        return new Date().after(expirationDate);
    }

}
