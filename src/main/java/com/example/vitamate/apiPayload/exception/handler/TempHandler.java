package com.example.vitamate.apiPayload.exception.handler;

import com.example.vitamate.apiPayload.code.BaseErrorCode;
import com.example.vitamate.apiPayload.exception.GeneralException;

public class TempHandler extends GeneralException{

    public TempHandler(BaseErrorCode errorCode){
        super(errorCode);
    }
}
