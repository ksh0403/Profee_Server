package com.example.Profee.entity;

import lombok.*;
import jakarta.persistence.*;
import java.sql.Timestamp;
import java.util.List;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;

@Entity
@Table(name = "users")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // AUTO_INCREMENT 적용

    @Column(name = "social_id", nullable = false, unique = true)
    private String socialId; // 소셜 로그인에서 받은 사용자 ID (예: Google ID, Kakao ID)

    @Column(name = "provider", nullable = false)
    private String provider; // 소셜 로그인 제공자 (Google, Kakao, Naver 등)

    @Column(name = "access_token", nullable = true)
    private String accessToken; // 액세스 토큰 (소셜 로그인 인증 후 받은 토큰)

    @Column(name = "name", nullable = true)
    private String name; // 사용자 이름 (옵션)

    @Column(name = "created_at")
    private Timestamp createdAt;

    @Column(name = "updated_at")
    private Timestamp updatedAt;

    @OneToMany(mappedBy = "user")
    private List<ChatRoomMember> chatRoomMembers; // User가 참여한 ChatRooms에 대한 관계

    @OneToMany(mappedBy = "sender")
    private List<Message> messages; // 사용자가 보낸 메시지들에 대한 관계
}
