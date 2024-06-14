package org.bedrock.hospinfosysserver;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
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
    public void putRegistInfo(@RequestParam Integer id, @RequestParam String realName, @RequestParam String gender, @RequestParam String cardNumber, @RequestParam String birthdate, @RequestParam Integer age,
                              @RequestParam String homeAddress, @RequestParam String deptName, @RequestParam String doctorName, @RequestParam String registLevel, @RequestParam String isBook,
                              @RequestParam Double registFee, @RequestParam String registDate, @RequestParam String diagnosis, @RequestParam String prescription, @RequestParam Double drugPrice,
                              @RequestParam int visitState) {

        logger.info("RegistInfo #{} post request received", id);

        RegistInfo registInfo = new RegistInfo(id, realName, gender, cardNumber,
                birthdate, age, homeAddress, deptName, doctorName, registLevel, isBook,
                registFee, registDate, diagnosis, prescription, drugPrice, visitState);

        registInfoRepository.putRegistInfo(registInfo);
    }
}
