package org.bedrock.hospinfosysserver;

import org.bedrock.utils.CustomUUIDGenerator;
import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

@Repository
public class RegistInfoController {

    final private RegistInfoRepository registInfoRepository;
    private final Logger logger = LoggerFactory.getLogger(RegistInfoController.class);

    public RegistInfoController(final RegistInfoRepository registInfoRepository) {
        this.registInfoRepository = registInfoRepository;
    }

    @GetMapping("api/registInfo")
    public RegistInfo getRegistInfo(@RequestParam Integer id) {
        logger.info("RegistInfo #{} retrieval request received", id);
        return registInfoRepository.getRegistInfo(id);
    }

    @PostMapping("api/registInfo")
    public ResponseEntity<String> putRegistInfo(@RequestBody final String registInfoJsonString) {

        JSONObject registInfoJson;
        try {
            registInfoJson = new JSONObject(registInfoJsonString);
        } catch (JSONException e) {
            return ResponseEntity
                    .badRequest()
                    .body("Error parsing registInfo JSON");
        }

        String id = CustomUUIDGenerator.generateUUID(8);
        String realName = registInfoJson.optString("realName");
        String gender = registInfoJson.optString("gender");
        String cardNumber = registInfoJson.optString("cardNumber");
        String birthdate = registInfoJson.optString("birthdate");
        Integer age = registInfoJson.optIntegerObject("age");
        String homeAddress = registInfoJson.optString("homeAddress");
        String deptName = registInfoJson.optString("deptName");
        String doctorName = registInfoJson.optString("doctorName");
        String registLevel = registInfoJson.optString("registLevel");
        String isBook = registInfoJson.optString("isBook");
        Double registFee = registInfoJson.optDouble("registFee");
        String registDate = registInfoJson.optString("registDate");
        String diagnosis = registInfoJson.optString("diagnosis");
        String prescription = registInfoJson.optString("prescription");
        Double drugPrice = registInfoJson.optDouble("drugPrice");
        Integer visitState = registInfoJson.optIntegerObject("visitState");


        logger.info("RegistInfo #{} post request received", id);

        RegistInfo registInfo = new RegistInfo(id, realName, gender, cardNumber,
                birthdate, age, homeAddress, deptName, doctorName, registLevel, isBook,
                registFee, registDate, diagnosis, prescription, drugPrice, visitState);

        registInfoRepository.putRegistInfo(registInfo);

        return ResponseEntity
                .ok()
                .body(id);
    }
}
