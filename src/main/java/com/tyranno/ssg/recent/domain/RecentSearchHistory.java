package com.tyranno.ssg.recent.domain;

import com.tyranno.ssg.auth.users.domain.Users;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "recent_search_history")
public class RecentSearchHistory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "users_id")
    private Users users;

    @Column(name = "search_word", nullable = false)
    private String searchWord;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;
}
