package com.example.vitamate.service;

import com.example.vitamate.domain.Supplement;
import com.example.vitamate.domain.SupplementDataSave;
import com.example.vitamate.domain.mapping.NutrientInfo;
import com.example.vitamate.repository.SupplementRepository;
import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.JAXBException;
import jakarta.xml.bind.Unmarshaller;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class OpenApiCommandService {

    private final SupplementRepository supplementRepository;

    @Transactional
    public void callApiAndSaveData(String apiKey, String startIdx, String endIdx) throws IOException {
        log.info("API Key = "+apiKey);
        String BASE_URL = "https://openapi.foodsafetykorea.go.kr/api";
        String keyId =  apiKey;
        String serviceId = "C003";
        String dataType = "xml";

        StringBuilder urlBuilder = new StringBuilder(BASE_URL);
        urlBuilder.append("/" + URLEncoder.encode(keyId, "UTF-8"));
        urlBuilder.append("/"+ URLEncoder.encode(serviceId, "UTF-8"));
        urlBuilder.append("/"+ URLEncoder.encode(dataType, "UTF-8"));
        urlBuilder.append("/"+ URLEncoder.encode(startIdx, "UTF-8"));
        urlBuilder.append("/"+ URLEncoder.encode(endIdx, "UTF-8"));

        URL url = new URL(urlBuilder.toString());
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();

        conn.setRequestMethod("GET");
        conn.setRequestProperty("Content-Type", "application/json");
        log.info("Request URL: " + urlBuilder.toString()); // URL 확인 로그 추가
        log.info("Response code:  " + conn.getResponseCode());

        BufferedReader rd;
        if(conn.getResponseCode() == 200 && conn.getResponseCode() <= 300){
            rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
        } else {
            rd = new BufferedReader(new InputStreamReader(conn.getErrorStream()));
        }

        StringBuilder sb = new StringBuilder();
        String line;

        while((line = rd.readLine()) != null){
            sb.append(line);
        }

        rd.close();
        conn.disconnect();

        String xml = sb.toString();
        Map<String, SupplementDataSave> result = new HashMap<>();
        try{
            JAXBContext jaxbContext = JAXBContext.newInstance(SupplementDataSave.class);
            Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
            SupplementDataSave apiResponse = (SupplementDataSave) unmarshaller.unmarshal(new StringReader(xml));
            result.put("response", apiResponse);

            if(apiResponse.getRow() != null){
                List<SupplementDataSave.Row> rowList = apiResponse.getRow();
                List<Supplement> supplementList = rowList.stream()
                        .map(this::convertToEntity)
                        .collect(Collectors.toList());

                log.info("Saving " + supplementList.size() + " supplements to the database.");
                supplementRepository.saveAll(supplementList);
                log.info("Data saved successfully.");
            } else {
                log.warn("No rows received in the API response.");
            }
        } catch (JAXBException e){
            log.error("Error during JAXB unmarshalling.", e);
//            e.printStackTrace();

        }

    }

    public Supplement convertToEntity(SupplementDataSave.Row response){
        log.info("Converting response to entity" + response);

        Supplement supplement = Supplement.builder()
                .brand(response.getBSSH_NM())
                .name(response.getPRDLST_NM())
                .build();

        List<NutrientInfo> nutrientInfoList = parseNutrients(response.getSTDR_STND(), supplement);
        supplement.setNutrients(nutrientInfoList);

        return supplement;
    }

    public List<NutrientInfo> parseNutrients(String stdrStnd, Supplement supplement){
        List<NutrientInfo> nutrientInfoList = new ArrayList<>();

        // 정규 표현식을 이용해 영양성분과 함량을 추출
        Pattern pattern = Pattern.compile(
                "([가-힣A-Za-z0-9\\s,]+?)\\s*:\\s*표시량\\s*\\(?(\\d+(?:,\\d{3})*(?:\\.\\d+)?)\\s?(mg|g|µg|ug|ugRE|㎍|mcg|㎍RE|mgNE|R.E|mgα-TE|ml)?\\/(?:(\\d+(?:,\\d{3})*(?:\\.\\d+)?)\\s?(mg|g|µg|ug|ugRE|㎍|mcg|㎍RE|mgNE|R.E|mgα-TE|ml)?)");
        Matcher matcher = pattern.matcher(stdrStnd);

        while (matcher.find()) {
            String nutrientName = matcher.group(1).trim();

            // 숫자와 공백 제거
            nutrientName = nutrientName.replaceAll("^\\d+\\s+", "").trim();
            // 특정 접두사 제거
            nutrientName = nutrientName.replaceAll("^총\\s+", "").trim();

            String amount = matcher.group(2) != null ? matcher.group(2).trim() : "";
            String unit = matcher.group(3) != null ? matcher.group(3).trim() : "";
            String totalAmount = matcher.group(4) != null ? matcher.group(4).trim() : "";
            String totalUnit = matcher.group(5) != null ? matcher.group(5).trim() : "";

            System.out.println("Nutrient Name: " + nutrientName);
            System.out.println("Amount: " + amount);
            System.out.println("Unit: " + unit);
            System.out.println("Total Amount: " + totalAmount);
            System.out.println("Total Unit: " + totalUnit);

            NutrientInfo nutrientInfo = NutrientInfo.builder()
                    .nutrientName(nutrientName)
                    .amount(amount)
                    .unit(unit)
                    .totalAmount(totalAmount)
                    .totalUnit(totalUnit)
                    .supplement(supplement)
                    .build();

            nutrientInfoList.add(nutrientInfo);
        }
        return nutrientInfoList;

    }

}
