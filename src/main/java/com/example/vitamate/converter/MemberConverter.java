package com.example.vitamate.converter;

import com.example.vitamate.domain.Member;
import com.example.vitamate.domain.enums.Gender;
import com.example.vitamate.domain.enums.MemberStatus;
import com.example.vitamate.web.dto.MemberRequestDTO;
import com.example.vitamate.web.dto.MemberResponseDTO;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class MemberConverter {

    public Member toMember(MemberRequestDTO.SignUpDTO request, List<String> roles){
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
                .coin(0)
                .nickname(request.getNickname())
                .status(MemberStatus.ACTIVE)
                .height(request.getHeight())
                .weight(request.getWeight())
                .roles(roles)
                .age(request.getAge())
                .bmr(bmr)
                .gender(gender)
                .build();
    }

    public MemberResponseDTO.SignUpResultDTO toSignUpResultDTO(Member member){
        return MemberResponseDTO.SignUpResultDTO.builder()
                .id(member.getId())
                .email(member.getEmail())
                .nickname(member.getNickname())
                .createdAt(member.getCreatedAt())
                .build();
    }
}
