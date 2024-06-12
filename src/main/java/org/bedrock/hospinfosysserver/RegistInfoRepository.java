package org.bedrock.hospinfosysserver;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Repository;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

@Repository
public class RegistInfoRepository {
    private Map<Integer, RegistInfo> registInfos;
    private final Logger logger = LoggerFactory.getLogger(RegistInfoRepository.class);

    public RegistInfoRepository() {
        registInfos = new HashMap<>();
    }

    public RegistInfo getRegistInfo(final int id) {
        return registInfos.get(id);
    }

    public void putRegistInfo(final RegistInfo registInfo) {
        registInfos.put(registInfo.id(), registInfo);
    }

    @Scheduled(fixedRate = 60000)
    public void saveRegistInfos() {
        try {
            logger.info("Saving regist infos...");
            FileOutputStream fos = new FileOutputStream("registInfo.dat");
            ObjectOutputStream oos = new ObjectOutputStream(fos);

            oos.writeObject(registInfos);
            oos.close();
            fos.close();
        } catch (Exception e) {
            logger.error("Error saving regist infos", e);
            throw new RuntimeException(e);
        }
    }

    public void loadRegistInfos() {
        try {
            FileInputStream fis = new FileInputStream("registInfo.dat");
            ObjectInputStream ois = new ObjectInputStream(fis);

            Object o = ois.readObject();
            //noinspection unchecked
            registInfos = (Map<Integer, RegistInfo>) o;
            ois.close();
            fis.close();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
