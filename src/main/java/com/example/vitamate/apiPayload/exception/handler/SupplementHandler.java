package com.example.vitamate.apiPayload.exception.handler;

import com.example.vitamate.apiPayload.code.BaseErrorCode;
import com.example.vitamate.apiPayload.exception.GeneralException;

public class SupplementHandler extends GeneralException {

    public SupplementHandler(BaseErrorCode errorCode){
        super(errorCode);
    }

}
