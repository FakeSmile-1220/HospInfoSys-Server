package org.bedrock.hospinfosysserver;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Repository;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.HashMap;
import java.util.Map;

@Repository
public class DoctorRepository {
    private Map<String, Doctor> doctors;
    private final Logger logger = LoggerFactory.getLogger(DoctorRepository.class);

    public DoctorRepository() {
        logger.info("Initializing DoctorRepository");
        doctors = new HashMap<>();
    }

    public Doctor getDoctor(final String id) {
        return doctors.get(id);
    }

    public void putDoctor(final Doctor doctor) {
        doctors.put(doctor.id(), doctor);
    }

    @Scheduled(fixedRate = 60000)
    public void saveDoctors() {
        try {
            logger.info("Saving doctors...");
            FileOutputStream fos = new FileOutputStream("doctor.dat");
            ObjectOutputStream oos = new ObjectOutputStream(fos);

            oos.writeObject(doctors);
            oos.close();
            fos.close();
        } catch (Exception e) {
            logger.error(e.getMessage());
            throw new RuntimeException(e);
        }
    }

    public void loadDoctors() {
        try {
            logger.info("Loading doctors...");
            FileInputStream fis = new FileInputStream("doctor.dat");
            ObjectInputStream ois = new ObjectInputStream(fis);

            Object o = ois.readObject();
            //noinspection unchecked
            doctors = (Map<String, Doctor>) o;
            ois.close();
            fis.close();
        } catch (Exception e) {
            logger.error(e.getMessage());
            throw new RuntimeException(e);
        }
    }
}
