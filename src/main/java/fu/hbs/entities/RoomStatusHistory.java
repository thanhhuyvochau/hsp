package fu.hbs.entities;

import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
@Entity
@Table(name = "room_status_history")
public class RoomStatusHistory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "room_status_history_id")
    private Long roomStatusHistoryId;
    private Long roomId;
    private Instant checkIn;
    private Instant checkOut;
}
