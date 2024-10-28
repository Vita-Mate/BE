package com.example.vitamate.aws.s3;

import java.io.IOException;

import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.example.vitamate.config.AmazonConfig;
import com.example.vitamate.domain.Uuid;
import com.example.vitamate.repository.UuidRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class AmazonS3Manager {

	private final AmazonS3 amazonS3;

	private final AmazonConfig amazonConfig;

	private final UuidRepository uuidRepository;


	public String uploadFile(String keyName, MultipartFile file){
		ObjectMetadata metadata = new ObjectMetadata();
		metadata.setContentLength(file.getSize());

		try{
			// PutObjectRequest를 파라미터로 받아 S3 버킷에 저장
			// 버킷의 디렉토리와 식별자는 KeyName으로 지정
			amazonS3.putObject(new PutObjectRequest(amazonConfig.getBucket(), keyName, file.getInputStream(), metadata));
		}catch (IOException e){
			log.error("error at AmazonS#Manaager uploadFile : {}", (Object) e.getStackTrace());
		}

		// 버킷에 저장된 파일의 url을 받아서 return
		return amazonS3.getUrl(amazonConfig.getBucket(), keyName).toString();
	}

	public String generateChallengeKeyName(Uuid uuid){
		return amazonConfig.getChallengePath() + '/' + uuid.getUuid();
	}
}
