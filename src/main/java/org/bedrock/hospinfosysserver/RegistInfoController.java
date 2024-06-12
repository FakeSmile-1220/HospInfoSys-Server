package org.bedrock.hospinfosysserver;

import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Repository
public class RegistInfoController {

    final private RegistInfoRepository registInfoRepository;

    public RegistInfoController(final RegistInfoRepository registInfoRepository) {
        this.registInfoRepository = registInfoRepository;
    }

    @GetMapping("api/registInfo")
    public RegistInfo getRegistInfo(@RequestParam Integer id) {
        return registInfoRepository.getRegistInfo(id);
    }

    @PostMapping("api/registInfo")
    public void setRegistInfo(@RequestParam final RegistInfo registInfo) {
        registInfoRepository.putRegistInfo(registInfo);
    }
}
