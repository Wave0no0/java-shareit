package ru.practicum.shareit.request.entity;

import jakarta.persistence.*;
import lombok.*;
import ru.practicum.shareit.user.entity.User;

import java.time.LocalDateTime;

@Entity
@Table(name = "item_request")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ItemRequest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 255, nullable = false)
    private String description;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "requestor_id", nullable = false)
    private User Author;

    @Column(nullable = false)
    private LocalDateTime created;
}
