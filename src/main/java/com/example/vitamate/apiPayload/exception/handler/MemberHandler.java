package com.example.vitamate.apiPayload.exception.handler;

import com.example.vitamate.apiPayload.code.BaseErrorCode;
import com.example.vitamate.apiPayload.exception.GeneralException;

public class MemberHandler extends GeneralException {

    public MemberHandler(BaseErrorCode errorCode){
        super(errorCode);
    }
}
