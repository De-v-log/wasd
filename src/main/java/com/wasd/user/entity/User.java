package com.wasd.user.entity;

import com.wasd.user.dto.UserDto;
import com.wasd.user.enums.MbtiEnum;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalTime;

@Entity
@Table(name="user_info")
@Builder
@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class User {

    @Id
    @Column(name = "user_id")
    private String userId;

    @Column(nullable = false, length = 255)
    private String email;

    @Column(nullable = false, length = 255)
    private String nickname;

    @Column(nullable = false, length = 10)
    private String provider;

//    @Lob
//    @Column(nullable = true)
    @Column(name="profile_img", columnDefinition = "TEXT", nullable = true)
    private String profileImg;

    @Column(name="year_of_birth", nullable = false)
    private int yearOfBirth;

    @Column(nullable = true, length = 4)
    private String mbti;

    @Column(name="start_time",nullable = true)
    private LocalTime startTime;

    @Column(name="end_time",nullable = true)
    private LocalTime endTime;

    public UserDto toDto(){
        return UserDto.builder()
                .userId(this.userId)
                .email(this.email)
                .nickname(this.nickname)
                .provider(this.provider)
                .profileImg(this.profileImg)
                .yearOfBirth(this.yearOfBirth)
                .mbti(this.mbti != null ? MbtiEnum.valueOf(this.mbti) : null)
                .startTime(this.startTime)
                .endTime(this.endTime)
                .build();
    }

}

