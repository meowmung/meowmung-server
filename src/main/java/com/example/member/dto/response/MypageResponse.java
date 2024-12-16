package com.example.member.dto.response;

import com.example.member.entity.Member;

public record MypageResponse(
        String mail,
        String nickname
) {
    public static MypageResponse fromEntity(Member member) {
        return new MypageResponse(
                member.getEmail(),
                member.getNickname()
        );
    }
}
