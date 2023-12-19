/*
 * Copyright (C) 2023, FPT University
 * SEP490 - SEP490_G77
 * HBS
 * Hotel Booking System
 *
 * Record of change:
 * DATE          Version    Author           DESCRIPTION
 * 22/10/2023    1.0        HieuLBM          First Deploy
 *
 *
 */
package fu.hbs.entities;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
@Entity
@Table(name = "transactions")
public class Transactions {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "transaction_id")
    private Long transactionId;
    private Long hotelBookingId;
    private Long paymentId;
    private String status;
    private BigDecimal amount;
    private String vnpayTransactionId;
    private Instant createdDate;
    private String content;
    private Long transactionTypeId;

}
