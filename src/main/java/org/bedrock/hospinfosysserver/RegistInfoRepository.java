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
    private final Logger logger = LoggerFactory.getLogger(RegistInfoRepository.class);
    private Map<String, RegistInfo> registInfos;

    public RegistInfoRepository() {
        registInfos = new HashMap<>();

        try {
            loadRegistInfos();
        } catch (FileNotFoundException e) {
            logger.info("Unable to find regist infos file, using default regist infos");
        } catch (Exception e) {
            logger.error("Unable to load regist infos file, using default regist infos");
        }
    }

    public RegistInfo getRegistInfo(final String id) {
        return registInfos.get(id);
    }

    public void putRegistInfo(final RegistInfo registInfo) throws IllegalStateException {
        if (registInfos.containsKey(registInfo.id())) {
            throw new IllegalStateException("RegistInfo with id " + registInfo.id() + " already exists");
        }
        registInfos.put(registInfo.id(), registInfo);
    }

    public void patchRegistInfo(final RegistInfo registInfo) {
        registInfos.put(registInfo.id(), registInfo);
    }

    @Scheduled(fixedRate = 60000)
    public void saveRegistInfos() {
        try {
//            logger.info("Saving regist infos...");
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

    public void loadRegistInfos() throws IOException, ClassNotFoundException {
        logger.info("Loading regist infos...");
        File file = new File("registInfo.dat");
        if (!file.exists()) {
            throw new FileNotFoundException("registInfo.dat not found");
        }
        FileInputStream fis = new FileInputStream("registInfo.dat");
        ObjectInputStream ois = new ObjectInputStream(fis);

        Object o = ois.readObject();
        //noinspection unchecked
        registInfos = (Map<String, RegistInfo>) o;
        ois.close();
        fis.close();
    }
}
