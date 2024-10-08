package com.wasd.user.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name="user_info")
@Builder
@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class User {

    @Id
    private String userId;

    @Column(nullable = false, length = 255)
    private String email;

    @Column(nullable = false, length = 255)
    private String nickname;

    @Column(nullable = false, length = 10)
    private String provider;

    @Lob
    @Column(nullable = true)
    private String profileImg;

    @Column(nullable = false)
    private int yearOfBirth;

    @Column(nullable = true, length = 4)
    private String mbti;

    @Column(nullable = true)
    private LocalDateTime startTime;

    @Column(nullable = true)
    private LocalDateTime endTime;
}

