package com.example.vitamate.converter;

import com.example.vitamate.domain.Member;
import com.example.vitamate.domain.enums.Gender;
import com.example.vitamate.domain.enums.MemberStatus;
import com.example.vitamate.web.dto.MemberRequestDTO;
import com.example.vitamate.web.dto.MemberResponseDTO;

import java.time.LocalDateTime;

public class MemberConverter {

    public static MemberResponseDTO.JoinResultDTO toJoinResultDTO(Member member) {
        return MemberResponseDTO.JoinResultDTO.builder()
                .memberId(member.getId())
                .createdAt(member.getCreatedAt())
                .build();
    }

    public static Member toMember(MemberRequestDTO.JoinDTO request) {
        Gender gender = null;

        switch (request.getGender()){
            case 1:
                gender = Gender.MALE;
                break;
            case 2:
                gender = Gender.FEMALE;
                break;
        }

        Integer bmr = null;
        if(request.getBmr() != null){
            bmr = request.getBmr();
        }else{
            Double LBM;
            if(gender == Gender.MALE){
                LBM = 0.407 * request.getWeight() + 0.267 * request.getHeight() - 19.2;
            }else{
                LBM = 0.252 * request.getWeight() + 0.473 * request.getHeight() - 48.3;
            }
            bmr = (int) (370 + 21.6 * LBM);
        }

        return Member.builder()
                .email(request.getEmail())
                .nickname(request.getNickname())
                .age(request.getAge())
                .height(request.getHeight())
                .weight(request.getWeight())
                .gender(gender)
                .bmr(bmr)
                .build();
    }
}
