package com.example.vitamate.apiPayload.exception.handler;

import com.example.vitamate.apiPayload.code.BaseErrorCode;
import com.example.vitamate.apiPayload.exception.GeneralException;

public class ChallengeHandler extends GeneralException {

	public ChallengeHandler(BaseErrorCode errorCode){
		super(errorCode);
	}
}
