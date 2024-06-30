package com.example.vitamate.web.dto;

import com.example.vitamate.domain.enums.Gender;
import lombok.Getter;

import java.util.List;

public class MemberRequestDTO {

    @Getter
    public static class JoinDTO{

        String email;
        String nickname;
        Integer age;
        Double height;
        Double weight;
        Integer gender;
        Integer bmr;
//        List<Long> preferCategory;
    }
}
