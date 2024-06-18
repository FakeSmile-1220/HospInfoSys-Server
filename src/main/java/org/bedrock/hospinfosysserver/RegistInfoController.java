package org.bedrock.hospinfosysserver;

import org.bedrock.utils.CustomUUIDGenerator;
import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.*;

@Repository
public class RegistInfoController {

    final private RegistInfoRepository registInfoRepository;
    private final Logger logger = LoggerFactory.getLogger(RegistInfoController.class);

    public RegistInfoController(final RegistInfoRepository registInfoRepository) {
        this.registInfoRepository = registInfoRepository;
    }

    private RegistInfo parseRegistInfo(final String json) throws JSONException {
        JSONObject registInfoJson;

        registInfoJson = new JSONObject(json);

        String id = registInfoJson.optString("id");
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

        RegistInfo registInfo = new RegistInfo(id, realName, gender, cardNumber,
                birthdate, age, homeAddress, deptName, doctorName, registLevel, isBook,
                registFee, registDate, diagnosis, prescription, drugPrice, visitState);

        return registInfo;
    }

    @GetMapping("api/registInfo")
    public RegistInfo getRegistInfo(@RequestParam String id) {
        logger.info("RegistInfo #{} retrieval request received", id);
        return registInfoRepository.getRegistInfo(id);
    }

    @PostMapping("api/registInfo")
    public ResponseEntity<String> putRegistInfo(@RequestBody final String registInfoJsonString) {
        RegistInfo registInfo;
        try {
            registInfo = parseRegistInfo(registInfoJsonString);
        } catch (JSONException e) {
            return ResponseEntity
                    .badRequest()
                    .body("Malformed JSON");
        }

        try {
            registInfoRepository.putRegistInfo(registInfo);
        } catch (IllegalStateException e) {
            return ResponseEntity
                    .unprocessableEntity()
                    .body("id already exists");
        }

        return ResponseEntity
                .ok()
                .body("RegistInfo successfully stored");
    }

    @PatchMapping("api/registInfo")
    public ResponseEntity<String> patchRegistInfo(@RequestBody final String registInfoJsonString) {
        RegistInfo registInfo;
        try {
            registInfo = parseRegistInfo(registInfoJsonString);
        } catch (JSONException e) {
            return ResponseEntity
                    .badRequest()
                    .body("Malformed JSON");
        }

        registInfoRepository.patchRegistInfo(registInfo);
        return ResponseEntity
                .ok()
                .body("RegistInfo successfully stored");
    }
}
