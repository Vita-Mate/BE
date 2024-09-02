package com.example.vitamate.service;

import com.example.vitamate.domain.SupplementDataSave;
import com.example.vitamate.domain.SupplementInfo;
import com.example.vitamate.repository.SupplementInfoRepository;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class OpenApiCommandService {

    private final SupplementInfoRepository supplementInfoRepository;

    @Transactional
    public void callApiAndSaveData(String apiKey, String startIdx, String endIdx) throws IOException {
        String BASE_URL = "https://openapi.foodsafetykorea.go.kr/api";
        String keyId =  apiKey; // e72b7472ca414bfc9ce9
        String serviceId = "C003";
        String dataType = "xml";
//        String startIdx = "1";
//        String endIdx = "10"; //3797

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
                List<SupplementInfo> supplementInfoList = rowList.stream()
                        .map(this::convertToEntity)
                        .collect(Collectors.toList());

                log.info("Saving " + supplementInfoList.size() + " supplements to the database.");
                supplementInfoRepository.saveAll(supplementInfoList);
                log.info("Data saved successfully.");
            } else {
                log.warn("No rows received in the API response.");
            }
        } catch (JAXBException e){
            log.error("Error during JAXB unmarshalling.", e);
//            e.printStackTrace();

        }

    }

    public SupplementInfo convertToEntity(SupplementDataSave.Row response){
        log.info("Converting response to entity" + response);
        SupplementInfo supplementInfo = SupplementInfo.builder()
                .BSSH_NM(response.getBSSH_NM())
                .STDR_STND(response.getSTDR_STND())
                .PRDLST_NM(response.getPRDLST_NM())
                .PRIMARY_FNCLTY(response.getPRIMARY_FNCLTY())
                .NTK_MTHD(response.getNTK_MTHD())
                .build();
        return supplementInfo;
    }


}
